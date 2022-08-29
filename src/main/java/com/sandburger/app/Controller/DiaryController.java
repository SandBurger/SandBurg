package com.sandburger.app.Controller;

import com.sandburger.app.DTO.DiaryDTO;
import com.sandburger.app.Service.DiaryService;
import com.sandburger.app.Util.CommonResponse;
import com.sandburger.app.Util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }


    @PostMapping(value = "/post")
    @ApiOperation(value = "post diary", notes = "posting each diary")
    public ResponseEntity postDiary(@RequestBody DiaryDTO.DiaryDefault diary, Authentication authentication){
        return diaryService.postDiary(diary.getDiary(), authentication.getName());
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "get diary by id", notes = "use id as pathvariable to get diary")
    public CommonResponse<DiaryDTO.DiaryOutput> getDiary(@PathVariable Long id){
        DiaryDTO.DiaryOutput output = diaryService.getDiaryById(id);

        return new CommonResponse<DiaryDTO.DiaryOutput>(output, HttpStatus.OK);
    }
}
