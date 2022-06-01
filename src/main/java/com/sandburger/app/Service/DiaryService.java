package com.sandburger.app.Service;

import com.sandburger.app.Entity.DiaryEntity;
import com.sandburger.app.Repository.DiaryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public ResponseEntity DiaryPostingService(String record, Integer sequence){
        try {
            DiaryEntity diary = DiaryEntity.builder().record(record).sequence(sequence).build();
            diaryRepository.save(diary);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (AssertionError assertionError){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }


}
