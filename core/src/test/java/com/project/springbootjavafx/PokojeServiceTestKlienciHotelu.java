package com.project.springbootjavafx;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.repositories.PokojeRepository;
import com.project.springbootjavafx.services.PokojeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code PokojeService#getKlienci(Pokoje)} method.
 */
@ExtendWith(MockitoExtension.class)
public class PokojeServiceTestKlienciHotelu {

    @Mock
    private PokojeRepository pokojeRepository;

    @InjectMocks
    private PokojeService pokojeService;

    private Pokoje pok1;
    private Klienci klient1;
    private Klienci klient2;

    @BeforeEach
    void setUp() {
        pok1 = new Pokoje();
        pok1.setId(1);

        klient1 = new Klienci();
        klient1.setId(1);

        klient2 = new Klienci();
        klient2.setId(2);

        // Link clients to the room
        klient1.setPokoj(pok1);
        klient2.setPokoj(pok1);
    }

    @Test
    void testGetKlienci() {

        // Given: a list of clients that are associated with the room (pok1)
        List<Klienci> expectedKlienci = Arrays.asList(klient1, klient2);
        when(pokojeRepository.getKlienciPokoju(1)).thenReturn(expectedKlienci);

        // When: we call the getKlienci method on the service with the room pok1
        List<Klienci> actualKlienci = pokojeService.getKlienci(pok1);

        // Then: the returned list of clients should match the expected list
        assertEquals(expectedKlienci, actualKlienci, "The list of clients should match the expected list.");
    }
}
