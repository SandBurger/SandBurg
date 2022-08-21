package com.sandburger.app.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UserDTO {

    @Getter
    @Setter
    public static class UserDefault {
        private String name;
        private String email;

        public UserDefault() {}

        @Builder
        public UserDefault(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    @Getter
    @Setter
    public static class UserOutput {
        private Long id;
        private String name;
        private String email;

        public UserOutput() {
        }

        @Builder
        public UserOutput(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

}
