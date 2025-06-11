package com.IQproject.court.config;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.CourtRepository;
import com.IQproject.court.repository.SurfaceTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Component that initializes default data in the database.
 * The initialization runs only if the `app.data-init` property is set to true.
 *
 * @author Vojtech Zednik
 */
@Component
public class DataInitializer {

    private final AppConfig config;
    private final SurfaceTypeRepository surfaceTypeRepository;
    private final CourtRepository courtRepository;

    /**
     * Constructor for DataInitializer.
     *
     * @param config                application configuration containing the data-init flag
     * @param surfaceTypeRepository repository for saving surface types
     * @param courtRepository       repository for saving courts
     */
    public DataInitializer(AppConfig config,
                           SurfaceTypeRepository surfaceTypeRepository,
                           CourtRepository courtRepository) {
        this.config = config;
        this.surfaceTypeRepository = surfaceTypeRepository;
        this.courtRepository = courtRepository;
    }

    @PostConstruct
    public void init() {
        if (!config.isDataInit()) return;

        SurfaceType clay = new SurfaceType("Clay", new BigDecimal(15));
        SurfaceType grass = new SurfaceType("Grass", new BigDecimal(10));
        surfaceTypeRepository.save(clay);
        surfaceTypeRepository.save(grass);

        courtRepository.save(new Court("Court 1", clay.getId()));
        courtRepository.save(new Court("Court 2", clay.getId()));
        courtRepository.save(new Court("Court 3", grass.getId()));
        courtRepository.save(new Court("Court 4", grass.getId()));
    }
}