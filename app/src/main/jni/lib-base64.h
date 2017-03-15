//
// Created by Andrew Chupin on 14.03.17.
//

#ifndef NATIVE_LIB_BASE64_H
#define NATIVE_LIB_BASE64_H

#include <stdint.h>
#define BASE64_BAD_CHAR -1
#define BASE64_BAD_INPUT_LENGTH -2
uint32_t base64_encoded_size(uint32_t cleartext_size) ;
int base64_decoded_size(uint8_t *b64) ;
int base64_encode(void *in, uint32_t in_length, void *out) ;
int base64_decode(void *in, uint32_t in_length, void *out) ;
#endif