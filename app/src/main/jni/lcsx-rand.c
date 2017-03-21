//
// Created by Andrew Chupin on 06.03.17.
//
#include <stdint.h>
#include "lcsx-rand.h"
#define RAND_A ((uint32_t)1103515245L)
#define RAND_C ((uint32_t)1292563L)
#define RAND_D ((uint32_t)362436001L)

/* Инициализация генератора двумя ключами: k1 и k2
 * Процедура инициализации гарантирует, что seed генератора shift-xor
 * будет ненулевым.
 */
void
random_lcsx_init(rnd_lcsx_ctx_t *context, uint32_t k1, uint32_t k2)
{
    context->lc = k1 ;
    context->sx = k2^RAND_D^k1 ;
    if (!context->sx) {
        context->sx = RAND_D ;
    }
    context->bc = 0 ;
}
/* Генератор является модифицированным генератором KISS
 *
 * Каждое сгенерированное 32битное число является суперпозицией по XOR
 * продуктов двух параллельных генераторов: линейного конгруэнтного генератора
 * и генератора shift-xor.
 *
 * Состояние генератора полностью хранится в описании контекста, потому
 * можно исполнять столько генераторов, сколько необходимо. Генератор пригоден
 * к исполнению в многопоточной программе.
 */
uint32_t
random_lcsx_v32(rnd_lcsx_ctx_t *context)
{
    context->lc = context->lc * RAND_A;
    context->lc += RAND_C;
    context->sx ^= context->sx << 17 ;
    context->sx ^= context->sx >> 13 ;
    context->sx ^= context->sx << 5 ;
    context->bc = 0 ;
    context->rv = context->lc ^ context->sx ;
    return context->rv ;
}
/* Функция возвращает следующий октет из последовательности.
 *
 * Функция запрашивает случайное число длиной 32 бита, возвращает его
 * по октетам за запрос начиная со старшего октета. После того, как
 * все число передано, функция обращается за следующим числом.
 */
uint8_t
random_lcsx_v8(rnd_lcsx_ctx_t *context) {
    uint8_t b ;
    if (!context->bc) {
        random_lcsx_v32(context) ;
        context->bc = 4 ;
    }
    if (context->bc == 4) {
        b = (uint8_t)(context->rv >> 24) ;
    } else if (context->bc == 3) {
        b = (uint8_t)((context->rv >> 16) & 0xFF) ;
    } else if (context->bc == 2) {
        b = (uint8_t)((context->rv >> 8) & 0xFF) ;
    } else if (context->bc == 1) {
        b = (uint8_t)(context->rv & 0xFF) ;
    }
    context->bc-- ;
    return b ;
}

