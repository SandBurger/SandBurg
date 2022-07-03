package com.sandburger.app.DTO;

import lombok.*;

@NoArgsConstructor
public class DiaryDTO {

    @Getter
    @Setter
    public static class DiaryDefault{
        private String diary;
        private Integer sequence;

        public DiaryDefault(){}

        @Builder
        public DiaryDefault(String diary, Integer sequence){
            this.diary = diary;
            this.sequence = sequence;
        }
    }

    @Getter
    @Setter
    public static class DiaryOutput{ // diary abstract class 추가 가능성
        private Long id;
        private String diary;
        private Integer sequence;

        public DiaryOutput(){
        }

        @Builder
        public DiaryOutput(String diary, Integer sequence, Long id){
            this.id = id;
            this.diary = diary;
            this.sequence = sequence;
        }
    }
}
