# IQ Puzzler Pro Solcer

## Program Description
Program ini merupakan program untuk mencari salah satu kemungkinan penyelesaian dari permainan IQ Puzzler Pro, yang mana tujuannya adalah membuat seluruh piece puzzle dapat mengisi seluruh papan yang ada tanpa ada papan yang belum terisi atau bagian piece puzzle yang tidak masuk ke papan. Cara penyelesaian yang dibuat dalam program ini dibuat menggunakan algoritma *brute-force* dengan menggunakan bahasa pemrograman java. Program ini akan membaca file input yang berisi ukuran papan, banyaknya potongan puzzle, jenis permainan, dan potongan-potongan puzzle yang dilambangkan dengan huruf A-Z. Program ini juga bisa menyimpan hasil solusi ke dalam file.

## Requirement
* Java versi 8 atau di atasnya

## Installation
1. Pastikan java sudah terinstall dan sesuai versinya (minimal versi 8).
   ```md
   java -version
   ```
2. Clone repositori ini
   ```md
   git clone https://github.com/tkanigara/Tucil1_13523055.git
   ```
3. Masuk ke direktori repositori

## Compile & Run
### Cara Compile
1. Masuk ke direktori `src` dimana file Main.java berada
2. Tulis di terminal:
  ```md
  javac Main.java -d ../bin
  ```
### Cara Menjalankan
1. Masuk ke direktori `bin` dimana file Main.class berada
2. Tulis di terminal:
   ```md
   java Main
   ```
3. Masukkan file input

## Input File's Format
Format dari input harus mengikuti aturan:
```md
N M P
MODE
SHAPE_1
SHAPE_2
...
SHAPE_P
```
* N × M : Ukuran papan permainan
* P : Jumlah bentuk puzzle
* MODE : "DEFAULT" atau mode lainnya
* SHAPE_X : Representasi bentuk menggunakan huruf

### Contoh
Berikut merupakan contoh file input dengan papan 5x5 dengan 7 bentuk puzzle
```md
5 5 7
DEFAULT
A
AA
B
BB
C
CC
D
DD
EE
EE
E
FF
FF
F
GGG
```

## Author
* Nama    : Muhammad Timur Kanigara
* NIM     : 13523055
* Github  : https://github.com/tkanigara

