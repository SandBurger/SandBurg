package com.sandburger.app.DTO;

import lombok.*;

@NoArgsConstructor
public class DiaryDTO {

    @Getter
    @Setter
    public static class DiaryDefault{
        private String diary;

        public DiaryDefault(){}

        @Builder
        public DiaryDefault(String diary){
            this.diary = diary;
        }
    }

    @Getter
    @Setter
    public static class DiaryOutput{ // diary abstract class 추가 가능성
        private Long id;
        private String diary;

        public DiaryOutput(){
        }

        @Builder
        public DiaryOutput(String diary, Long id){
            this.id = id;
            this.diary = diary;
        }
    }
}
