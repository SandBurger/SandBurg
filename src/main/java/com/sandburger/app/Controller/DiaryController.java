package com.sandburger.app.Controller;

import com.sandburger.app.DTO.DiaryDTO;
import com.sandburger.app.Service.DiaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }


    @PostMapping
    @ApiOperation(value = "post diary", notes = "posting each diary")
    public ResponseEntity PostDiary(@RequestBody DiaryDTO.Diary diary){
        return diaryService.DiaryPostingService(diary.getDiary(), diary.getSequence());
    }
}
