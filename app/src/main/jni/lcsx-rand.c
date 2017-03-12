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
    uint32_t a, b ;
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
#ifdef RANDOM_LCSX_TEST
/* Функция самотестирования
 *
 * Создает генератор, после заданного числа срабатываний сравнивает выдачу
 * генератора с эталонами.
 *
 * Возвращает:
 * 0 — в случае успеха,
 * -1 – не пройден тест 32битной последовательности
 * -2 – не пройден тест 8 битной последовательности
 */
#define LCSX_K1 0
#define LCSX_K2 0
#define LCSX_NUM_DROPS 100000
#define LCSX_R1
#define LCSX_R2
#define LCSX_R3
#define LCSX_R4
#define LCSX_R5
static
0x8C1C6D11
0x58559F6F
0x92E1CE9A
0xE350160C
0x5356C933
uint8_t r8_e[] = {
0xCC, 0x79, 0x61, 0x29, 0xA5, 0xE6, 0xA0, 0x6D, 0xA8, 0xF4, 0x59, 0xA1, 0x41, 0x6A, 0x2C, 0x3C, 0x3C, 0x53, 0xDE, 0x40, 0x4D, 0x01, 0x73, 0x7F, 0xFE, 0xC8, 0x0A, 0xEF, 0xFD, 0x31, 0xDF, 0x0B, 0x70, 0xA0, 0x47, 0xD5, 0xBF, 0xFA, 0x39, 0x54, 0x92, 0x0E, 0xF6, 0x7D, 0xA2, 0xCF, 0x4F, 0x7E, 0x46, 0xC3, 0x9A, 0xA5, 0x79, 0x02, 0xC4, 0xBF, 0xCE, 0x23, 0x94, 0xD1, 0x44, 0x46, 0x44, 0xA9
};
int
random_lcsx_test(void) {
     rnd_lcsx_ctx_t rc ;
     uint32_t rnd_value ;
     uint8_t r8 ;
     int i ;
    random_lcsx_init(&rc, LCSX_K1, LCSX_K2) ;
    for (i = 0; i<LCSX_NUM_DROPS; i++) {
     random_lcsx_v32(&rc) ;
    }
    rnd_value = random_lcsx_v32(&rc) ;
    if ( rnd_value != LCSX_R1 ) {
return -1 ; }
    rnd_value = random_lcsx_v32(&rc) ;
    if ( rnd_value != LCSX_R2 ) {
return -1 ; }
    rnd_value = random_lcsx_v32(&rc) ;
    if ( rnd_value != LCSX_R3 ) {
return -1 ; }
    rnd_value = random_lcsx_v32(&rc) ;
    if ( rnd_value != LCSX_R4 ) {
return -1 ;
60
Комплекс дистанционного управления оборудованием. Протоколы мобильное устройство ↔ сервер.

}
    rnd_value = random_lcsx_v32(&rc) ;
    if ( rnd_value != LCSX_R5 ) {
return -1 ; }
     for (i=0; i<sizeof(r8_e); i++) {
         r8 = random_lcsx_v8(&rc) ;
         if (r8 != r8_e[i]) {
return -2 ; }
}
return 0 ; }
#ifdef RANDOM_LCSX_TEST_MAIN
#include <stdio.h>
int
main(void) {
     if (random_lcsx_test()) {
         printf ("Failed\n") ;
         return 4 ;
     }
     printf ("Passed\n") ;
     return 0 ;
}
#endif
#endif
