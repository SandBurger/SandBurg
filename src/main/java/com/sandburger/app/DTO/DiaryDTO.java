package com.sandburger.app.DTO;

import lombok.*;

@NoArgsConstructor
public class DiaryDTO {

    @Getter
    @Setter
    public static class Diary{
        private String diary;
        private Integer sequence;

        public Diary(){}

        @Builder
        public Diary(String diary, Integer sequence){
            this.diary = diary;
            this.sequence = sequence;
        }
    }
}
