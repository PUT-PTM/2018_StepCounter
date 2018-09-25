#include "stm32f4xx.h"
#include "stm32f4_discovery.h"
#include "tm_stm32f4_lis302dl_lis3dsh.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int p=0;
int x[4],y[4],z[4];
int x_result,y_result,z_result;
int x_max,y_max,z_max;
int x_min,y_min,z_min;
int MAX_VALUE=100;
int x_th,y_th,z_th;
int x_old,y_old,z_old;
int x_new,y_new,z_new;
int x_th_abs,y_th_abs,z_th_abs;
TM_LIS302DL_LIS3DSH_t 	Axes_Data;

int main(void)
{
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOC, ENABLE);
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_USART3, ENABLE);

	GPIO_InitTypeDef GPIO_InitStructure;
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_10 | GPIO_Pin_11;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_UP;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOC, &GPIO_InitStructure);

	GPIO_PinAFConfig(GPIOC, GPIO_PinSource10, GPIO_AF_USART3);
	GPIO_PinAFConfig(GPIOC, GPIO_PinSource11, GPIO_AF_USART3);

	USART_InitTypeDef USART_InitStructure;
	USART_InitStructure.USART_BaudRate = 9600;
	USART_InitStructure.USART_WordLength = USART_WordLength_8b;
	USART_InitStructure.USART_StopBits = USART_StopBits_1;
	USART_InitStructure.USART_Parity = USART_Parity_No;
	USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
	USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;

	USART_Init(USART3, &USART_InitStructure);

	USART_Cmd(USART3, ENABLE);

	TM_LIS302DL_LIS3DSH_Init(TM_LIS3DSH_Sensitivity_2G,TM_LIS3DSH_Filter_800Hz);

	for(;;){
		TM_LIS302DL_LIS3DSH_ReadAxes(&Axes_Data);
            /*SUM FILTERING*/

            /*NOT USED A LOOP FOR STM STUDIO*/
            x[0] = Axes_Data.X;
            y[0] = Axes_Data.Y;
            z[0] = Axes_Data.Z;

            x[1] = Axes_Data.X;
            y[1] = Axes_Data.Y;
            z[1] = Axes_Data.Z;

            x[2] = Axes_Data.X;
            y[2] = Axes_Data.Y;
            z[2] = Axes_Data.Z;

            x[3] = Axes_Data.X;
            y[3] = Axes_Data.Y;
            z[3] = Axes_Data.Z;

            x_result = x[0] + x[1] + x[2] + x[3];
            y_result = y[0] + y[1] + y[2] + y[3];
            z_result = z[0] + z[1] + z[2] + z[3];
            /*END SUM FILTERING*/

            /*FIND MAX AND MIN VALUE*/
            if (x_result > x_max) x_max = x_result;
            if (x_result < x_min) x_min = x_result;

            if (y_result > y_max) y_max = y_result;
            if (y_result < y_min) y_min = y_result;

            if (z_result > z_max) z_max = z_result;
            if (z_result < z_min) z_min = z_result;
            /*END FIND MAX AND MIN VALUE*/

            p++;

            if (p > 50) {
                /*COMPUTE THRESHOLD*/
                p = 0;
                x_th = (x_max + x_min) / 2;
                y_th = (y_max + y_min) / 2;
                z_th = (z_max + z_min) / 2;

                /*INIT VALUE*/
                x_max = -MAX_VALUE;
                x_min = MAX_VALUE;
                y_max = -MAX_VALUE;
                y_min = MAX_VALUE;
                z_max = -MAX_VALUE;
                z_min = MAX_VALUE;
                /*END INIT VALUE*/
            }

            x_old = x_new;
            y_old = y_new;
            z_old = z_new;
            /*DISCARD SMALL ACCEL*/
            if (abs(x_new - x_result) > 100)
                x_new = x_result;
            if (abs(y_new - y_result) > 100)
                y_new = y_result;
            if (abs(z_new - z_result) > 100)
                z_new = z_result;



            /*FIND LARGEST ACCEL AXIS*/
            x_th_abs = abs(x_th);
            y_th_abs = abs(y_th);
            z_th_abs = abs(z_th);

            if (x_th_abs > y_th_abs) {
                /*x > y*/
                if (x_th_abs > z_th_abs) {

                    /*x is the largest*/
                    if (x_old > x_th && x_new < x_th)
                    {
            			while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
            			USART_SendData(USART3, '1');
            			while (USART_GetFlagStatus(USART3, USART_FLAG_TC) == RESET);
                    }

                } else {

                    /*z is the largest*/
                    if (z_old > z_th && z_new < z_th)
                    {
            			while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
            			USART_SendData(USART3, '1');
            			while (USART_GetFlagStatus(USART3, USART_FLAG_TC) == RESET);
                    }

                }
            } else {
                /*y > x*/
                if (y_th_abs > z_th_abs) {
                    /*y is the largest*/
                    if (y_old > y_th && y_new < y_th)
                    {
            			while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
            			USART_SendData(USART3, '1');
            			while (USART_GetFlagStatus(USART3, USART_FLAG_TC) == RESET);
                    }
                } else {
                    /*z is the largest*/
                    if (z_old > z_th && z_new < z_th)
                    {
            			while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
            			USART_SendData(USART3, '1');
            			while (USART_GetFlagStatus(USART3, USART_FLAG_TC) == RESET);
                    }
                }
            }
        }
    }

