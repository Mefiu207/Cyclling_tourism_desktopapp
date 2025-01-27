package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.Hotele;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.HoteleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoteleService extends AbstractServices<Hotele, String>{

    // Trzyma 2 kopie tego sameg repisitory aby móc korzystać z metod w hoteleRepository nigdy więcej nie rób ID Stringiem
    private HoteleRepository hoteleRepository;

    @Autowired
    public HoteleService(HoteleRepository hoteleRepository) {
        super(hoteleRepository, Hotele.class, String.class);
        this.hoteleRepository = hoteleRepository;
    }

    @Override
    public Hotele add(Hotele hotel) throws DuplicatedEntityExceptionn, WrongCodeLengthException {

        if(hoteleRepository.existsByKod(hotel.getKod())){
            throw new DuplicatedEntityExceptionn("Hotel o kodzie " + hotel.getKod()+ " już istnieje");
        }
        else if(hotel.getKod().trim().length() != 6){
            throw new WrongCodeLengthException("Za krótki lub za długi kod hotelu (ma być 6 znaków)");
        }

        return hoteleRepository.save(hotel);
    }

    public List<Hotele> getHoteleMiasta(Miasta miasto){
        return hoteleRepository.getHoteleByMiasto(miasto.getMiasto());
    }
}
