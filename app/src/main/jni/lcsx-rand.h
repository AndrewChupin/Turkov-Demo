//
// Created by Andrew Chupin on 06.03.17.
//

#ifndef __LCSX_RAND_H
#define __LCSX_RAND_H
typedef struct {
    uint32_t lc ;
    uint32_t sx ;
    uint32_t rv ;
    uint8_t  bc ;
} rnd_lcsx_ctx_t ;
/* seed для линейного конгруэнтного генератора */
/* seed для shift-xor генератора */
/* предыдущее сгенерированное число (буфер на 4 байта) */
/* счетчик еще не отданных октетов */
    void random_lcsx_init(rnd_lcsx_ctx_t *context, uint32_t k1, uint32_t k2) ;
    uint32_t random_lcsx_v32(rnd_lcsx_ctx_t *context) ;
    uint8_t random_lcsx_v8(rnd_lcsx_ctx_t *context) ;
#endif
