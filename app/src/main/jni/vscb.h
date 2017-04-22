//
// Created by Andrew Chupin on 25.03.17.
//

#ifndef NATIVE_МЫСИ_H
#define NATIVE_МЫСИ_H

#include <stdint.h>

typedef struct { uint16_t key_size ; uint16_t key_pos ; uint8_t *key ;
} vscb_ctx_t ;
enum {
    VSCB_SUCCESS = 0,
    VSCB_SHORT_KEY
};
#ifndef NULL
#define NULL ((void *)0)
#endif
void vscb_init(vscb_ctx_t *context, uint8_t *aKey, uint16_t keySize ) ;
int vscb_encrypt( vscb_ctx_t *context, void *clear_text, uint16_t text_size, void
*encrypted) ;
int vscb_decrypt( vscb_ctx_t *context, void *encrypted, uint16_t text_size, void
*clear_text) ;
int hex2bin(unsigned char *x, int length);
void dump(uint8_t *x, int xl);
#endif
