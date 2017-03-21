//
// Created by Andrew Chupin on 14.03.17.
//

#include <stdint.h>
#include "lib-base64.h"
static
const
uint8_t alphabet64[]=
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
static
int
decode_char(uint8_t in, uint8_t *out) {
    if (in <= 'Z' && in >= 'A') {
        *out = in - 'A' ;
    } else if (in >= 'a' && in <= 'z') {
        *out = (in -'a') + 26 ;
    } else if (in >= '0' && in <= '9') {
        *out = (in - '0') + 52 ;
    } else if (in == '+') {
        *out = 62 ;
    } else if (in == '/') {
        *out = 63 ;
    } else if (in == '=') {
        *out = 0 ;
    } else {
        return BASE64_BAD_CHAR ;
    }
    return 0 ; }

/* Простой калькулятор длины выходного закодированного буфера по длине
 * входного. Терминирующий ноль не входит в вычисления */
uint32_t
base64_encoded_size(uint32_t cleartext_size) {
    return ((cleartext_size+2)/3)*4 ;
}

/* Калькулятор длины декодированного двоичного сообщения по содержимому
кодированного буфера. Внимание! Длина кодированного буфера вычисляется как
длина нуль-терминированной строки в стандарте языка "C".
*/
int
base64_decoded_size(uint8_t *b64) {
    uint32_t l ;
    uint32_t i ;
    for (i = 0; b64[i] != '\0'; i++) ;
    if (!i) {
        return 0 ; }
    l = (i/4) ;
    if (l * 4 != i) {
        return BASE64_BAD_INPUT_LENGTH ;
    }
    l *= 3 ;
    i --;
    if (b64[i] == '=') {
        l-- ; }
    i --;
    if (b64[i] == '=') {
        l-- ; }
    return l ; }

/* Кодирует 3 входных байта в 4 выходных в кодировке BASE64
 * Для внутреннего употребления
 * Неиспользованные байты inb ДОЛЖНЫ БЫТЬ заполнены нулями */
static
void
encode_block(uint8_t *inb, uint8_t len, uint8_t *outb) {
    outb[0] = alphabet64[ (inb[0] >> 2) ];
    outb[1] = alphabet64[ (((inb[0] & 0x03) << 4) | ((inb[1] & 0xf0) >> 4)) ];
    if (len > 1) {
        outb[2] = alphabet64[ (((inb[1] & 0x0f) << 2) | ((inb[2] & 0xc0) >>
                                                         6)) ] ;
    } else {
        outb[2] = '=';
    }
    if (len > 2) {
        outb[3] = alphabet64[ (inb[2] & 0x3f) ] ;
    } else {
        outb[3] = '=' ;
    }
}


/* Декодирует 4 входных байта в кодировке BASE64 в 3 выходных
 * Для внутреннего употребления
 */
static
int
decode_block(uint8_t *inb, uint8_t *outb) {
    int len ;
    int err ;
    uint8_t inbc[3] ;
    len = 3 ;
    if (inb[2] == '=') {

        len = 1 ;
    } else if (inb[3] == '=') {
        len = 2 ;
    } else if (inb[1] == '=' || inb[0] == '=') {
        return BASE64_BAD_CHAR ;
    }
    err =
            decode_char(inb[0], &inbc[0]) ||
            decode_char(inb[1], &inbc[1]) ||
            decode_char(inb[2], &inbc[2]) ||
            decode_char(inb[3], &inbc[3]) ;
    if (err) {
        return err ;
    }
    outb[0] = (uint8_t) (((inbc[0] << 2) & 0xFC) | ((inbc[1] >> 4) & 0x03));
    if (len > 1) {
        outb[1] = (uint8_t) (((inbc[1] << 4) & 0xF0) | ((inbc[2] >> 2) & 0x0F));
        if (len >2) {
            outb[2] = (uint8_t) (((inbc[2] << 6) & 0xC0) | (inbc[3] & 0x3F));
        }
    }
    return 0 ; }
/* Кодирование входных данных в нультерминированную строку BASE64.
 * Выходной буфер out должен быть достаточной длины, чтобы поместилось вся
 * закодированная строка и терминирующий ноль.
 */
int
base64_encode(void *in, uint32_t in_length, void *out) {
    uint8_t *i, *o ;
    i = (uint8_t *)in ;
    o = (uint8_t *)out ;
    *o = '\0' ;
    while (in_length >= 3) {
        encode_block(i, 3, o) ;
        i += 3 ;
        o += 4 ;
        *o = '\0' ;
        in_length -= 3 ;
    }
    if (in_length) {
        encode_block(i, (uint8_t)in_length, o) ;
        o += 4 ;
        *o = '\0' ;
    }
    return 0 ;
}

/* Декодирование входных данных BASE64 в двоичные данные.
 * Выходной буфер out должен быть достаточной длины, чтобы поместились все
 * декодированные данные.
 */
int
base64_decode(void *in, uint32_t in_length, void *out) {
    uint8_t *i, *o ;
    int err ;
    i = (uint8_t *)in ;
    o = (uint8_t *)out ;

    while (in_length >= 4) {
        if (err = decode_block(i, o)) {
            return err ; }
        i += 4 ;
        o += 3 ;
        in_length -= 4 ;
    }
    if (in_length) {
        return BASE64_BAD_INPUT_LENGTH ;
    }
    return 0 ; }

#ifdef BASE64_TEST
#include <stdio.h>
#include <string.h>
#define NUMTESTS 7
struct b64Test_s {
     char *in;
uint32_t inl; char *out ; uint32_t outl ;
} td [] = {
     { "", 0, "", 0 },
     { "f", 1, "Zg==", 4 } ,
     { "fo", 2, "Zm8=", 4 },
     { "foo", 3, "Zm9v", 4},
     { "foob", 4, "Zm9vYg==", 8},
     {"fooba", 5, "Zm9vYmE=", 8},
     {"foobar", 6, "Zm9vYmFy",8}
};
void
main(void) {
     int i ;
     uint8_t buf[80] ;
     for(i = 0; i<NUMTESTS; i++) {
         printf("%d:\n", i) ;
         printf("\t[%s] expected: [%s] ", td[i].in, td[i].out) ;
         if (base64_encoded_size(td[i].inl) != td[i].outl) {
              printf("Failed out data length. ") ;
         }
         memset(buf, '\0', sizeof(buf)) ;
         base64_encode(td[i].in, td[i].inl, buf) ;
         printf("Got: [%s] * ", buf) ;
         if (strcmp(buf, td[i].out)) {
              printf("Failed\n\t") ;
         } else {
              printf("Passed\n\t") ;
         }
         /*************************/
         printf("\t[%s] expected: [%s] ", td[i].out, td[i].in) ;
         if (base64_decoded_size(td[i].out) != td[i].inl) {
              printf("Failed out data length. ") ;
         }
          memset(buf, '\0', sizeof(buf)) ;
          base64_decode(td[i].out, td[i].outl, buf) ;
          printf("Got: [%s] * ", buf) ;

if (strcmp(buf, td[i].in)) {
     printf("Failed\n") ;
} else {
     printf("Passed\n") ;
}

} }
#endif