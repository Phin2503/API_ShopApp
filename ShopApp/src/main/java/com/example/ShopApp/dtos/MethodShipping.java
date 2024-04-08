package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MethodShipping {

        NORMAL(1),
        FAST(2),
        EXPRESS(3);


        private final int value;

         MethodShipping(int value) {
            this.value = value;
        }


        @JsonValue
        public int getValue() {
            return value;
        }

        public static MethodShipping fromValue(int value) {
            for (MethodShipping methodShipping : MethodShipping.values() ){
                if ( methodShipping.value == value) {
                    return methodShipping;
                }
            }
            return null;
        }
    }

