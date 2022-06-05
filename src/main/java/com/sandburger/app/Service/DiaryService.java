package com.sandburger.app.Service;

import com.sandburger.app.DTO.DiaryDTO;
import com.sandburger.app.Entity.DiaryEntity;
import com.sandburger.app.Repository.DiaryRepository;
import com.sandburger.app.Util.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public ResponseEntity postDiary(String record, Integer sequence){
        try {
            DiaryEntity diary = DiaryEntity.builder().record(record).sequence(sequence).build();
            diaryRepository.save(diary);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (AssertionError assertionError){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public DiaryDTO.DiaryOutput getDiaryById(Long id){
        try{
            DiaryEntity diary = diaryRepository.findById(id).get();
            DiaryDTO.DiaryOutput diaryOutput = DiaryDTO.DiaryOutput.builder().id(diary.getDiary_idx()).diary(diary.getRecord()).sequence( diary.getSequence()).build();
            return diaryOutput;
        }
        catch(EntityNotFoundException e){
            return null; // validation 추가되어야함.
        }
    }




}
