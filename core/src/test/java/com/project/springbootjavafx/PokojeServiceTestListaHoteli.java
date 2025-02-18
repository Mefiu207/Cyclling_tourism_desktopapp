package com.project.springbootjavafx;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.repositories.PokojeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PokojeService} methods setListaHoteliTrue and setListaHoteliFalse.
 */
@ExtendWith(MockitoExtension.class)
public class PokojeServiceTestListaHoteli {

    @Mock
    private PokojeRepository pokojeRepository;

    @InjectMocks
    private PokojeService pokojeService;

    private Pokoje pokoj1;
    private Pokoje pokoj2;
    private List<Pokoje> pokojeList;

    @BeforeEach
    void setUp() {
        // Create sample Pokoje instances with assigned IDs.
        pokoj1 = new Pokoje();
        pokoj1.setId(1);
        pokoj2 = new Pokoje();
        pokoj2.setId(2);
        pokojeList = Arrays.asList(pokoj1, pokoj2);
    }

    @Test
    void testSetListaHoteliTrue() {
        // When: setListaHoteliTrue is called with a list of rooms.
        pokojeService.setListaHoteliTrue(pokojeList);

        // Then: verify that updateListaHoteliTrue is called for each room ID exactly once.
        verify(pokojeRepository, times(1)).updateListaHoteliTrue(1);
        verify(pokojeRepository, times(1)).updateListaHoteliTrue(2);
    }

    @Test
    void testSetListaHoteliFalse() {
        // When: setListaHoteliFalse is called with a list of rooms.
        pokojeService.setListaHoteliFalse(pokojeList);

        // Then: verify that updateListaHoteliFalse is called for each room ID exactly once.
        verify(pokojeRepository, times(1)).updateListaHoteliFalse(1);
        verify(pokojeRepository, times(1)).updateListaHoteliFalse(2);
    }
}
