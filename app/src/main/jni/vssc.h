//
// Created by Andrew Chupin on 06.03.17.
//


#ifndef __VSSC_H
#define __VSSC_H

#include "lcsx-rand.h"

typedef struct {
    rnd_lcsx_ctx_t  random_context ;
} vssc_ctx_t ;
#ifndef NULL
#define NULL ((void *)0)
#endif
void vssc_init(vssc_ctx_t *context, uint32_t k1, uint32_t k2) ;
void vssc_encrypt(
        vssc_ctx_t *context,
        void *clear_text,
        uint32_t text_size,
        void *encrypted) ;
void vssc_decrypt(
        vssc_ctx_t *context,
        void *encrypted,
        uint32_t text_size,
        void *clear_text) ;
#ifdef VSSC_TEST
int vssc_test(void) ;
#endif
#endif