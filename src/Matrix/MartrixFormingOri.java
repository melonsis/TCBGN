package Matrix;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import BGN.PublicKey;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import BGN.PublicKey;
import BGN.TCBGN;

import java.util.Vector;

public class MartrixFormingOri {
    public int Bottom;
    public int Top;
    public int maxNum;
    public Vector matrixForming(MartrixFormingOri RNG1)
    {
        int matrixOrder=1;
        int i=0;
        int j=0;
        int max=0;
        Vector<int[][]> matrices = new Vector();
        while(RNG1.maxNum>1)
        {
            if((i*i)-RNG1.maxNum<0)
                i++;
            else
                break;
        }
        matrixOrder=i;
        int[][] matrix = new int[matrixOrder][matrixOrder];
        i=0;
        for (i=0;i<matrixOrder;i++)
        {
            for (j=0;j<matrixOrder;j++)
            {
                if ((i*matrixOrder+j+1>=RNG1.Bottom)&&(i*matrixOrder+j+1<=RNG1.Top))
                {
                    matrix[i][j]=1;
                }
            }
        }
        matrices.add(matrix);
        int R1I=(RNG1.Bottom)/matrixOrder;
        int R1B=(RNG1.Bottom)%matrixOrder;
        int[][] matrix1=matrix1Forming(R1B,R1I,matrixOrder);
        int R2I=RNG1.Top/matrixOrder;
        int R2B=RNG1.Top%matrixOrder;
        int[][] matrix2=matrix2Forming(R2B,R2I,matrixOrder);
        int[][] matrix3=matrix3Forming(R1B,R1I,R2B,R2I,matrixOrder);
        if (R1B!=1)
        {
            matrices.add(matrix1);
            matrices.add(matrix2);
            matrices.add(matrix3);
        }
        else
        {
            matrices.add(matrix3);
            matrices.add(matrix2);
            matrices.add(matrix1);
        }

        return matrices;
    }
    public int[][] matrix1Forming(int R1B,int R1I,int matrixOrder) //Form an R1 for specify matrix.
    {
        int[][] matrix1=new int[matrixOrder][matrixOrder];
        int i=0;
        int j=0;
        if(R1B!=0)  //For simplify, when a matrix's R1 should be empty, R1 become R3(CR), and R3 will become empty instead.
        {
            for(j=R1B-1;j<matrixOrder;j++)
            {
                matrix1[R1I][j]=1;
                //matrix3[R1I][j]=0;
            }
        }
        if (R1B==0)
        {
            matrix1[R1I-1][matrixOrder-1]=1;
            //matrix3[R1I][j]=0;
        }
        return matrix1;
    }
    public int[][] matrix2Forming(int R2B,int R2I,int matrixOrder)
    {
        int[][] matrix2=new int[matrixOrder][matrixOrder];
        int j=0;
        for(j=0;j<R2B;j++)
        {
            matrix2[R2I][j]=1;
            //matrix3[R2I][j]=0;
        }
        return matrix2;
    }
    public int[][] matrix3Forming(int R1B,int R1I,int R2B,int R2I,int matrixOrder)
    {
        int[][] matrix3=new int[matrixOrder][matrixOrder];
        int i=0;
        int j=0;
        if (R1B!=0) //For simplify, when a matrix's R1 should be empty, R1 become R3(CR), and R3 will become empty instead.
            for(i=R1I+1;i<R2I;i++)
            {
                for (j=0;j<matrixOrder;j++)
                    matrix3[i][j]=1;
            }
        if (R1B==0)
            for(i=R1I;i<R2I;i++)
            {
                for (j=0;j<matrixOrder;j++)
                    matrix3[i][j]=1;
            }
        return matrix3;
    }
    public int[] yForming(int[][] matrix,int matrixOrder)
    {
        int[] yVector=new int[matrixOrder];
        int i,j=0;
        for(j=0;j<matrixOrder;j++)
        {
            yVector[j]=1;
            for (i=0;i<matrixOrder;i++)
            {
                if (matrix[i][j]==1)
                {
                    yVector[j]=0;
                    break;
                }
            }
        }
        return yVector;
    }
    public int[] xpForming(int[][] matrix,int matrixOrder)
    {
        int[] xpVector=new int[matrixOrder];
        int i,j=0;
        for(i=0;i<matrixOrder;i++)
        {
            xpVector[i]=1;
            for (j=0;j<matrixOrder;j++)
            {
                if (matrix[i][j]==1)
                {
                    xpVector[i]=0;
                    break;
                }
            }
        }
        return xpVector;
    }
    public Element[] vectorEncrypt(int[] oriVector,PublicKey BGNPK)
    {
        Element[] encVector=new Element[oriVector.length];
        TCBGN BGN=new TCBGN();
        int i=0;
        for (i=0;i<oriVector.length;i++)
        {
            encVector[i]=BGN.Encrypt(BGNPK,oriVector[i]);
        }
        return encVector;

    }
    public void setBottom(int bottom) {
        Bottom = bottom;
    }

    public int getBottom() {
        return Bottom;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public int getTop() {
        return Top;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setTop(int top) {
        Top = top;
    }

public static void main(String[] args){
        TCBGN bgn = new TCBGN();
        PublicKey BGNPK=bgn.gen(512);
        MartrixFormingOri RNG1 =new MartrixFormingOri();
        RNG1.Bottom=50;
        RNG1.Top=60;
        RNG1.maxNum=900;
        int i=0;
        double t1=System.currentTimeMillis();
        Vector<int[][]> matrices = RNG1.matrixForming(RNG1);
        System.out.println("R:");
        for(int[] a:matrices.get(0))
        {
            for(int b:a)
            {
            System.out.print(b+" ");
            }
        System.out.println();//换行
        }
    System.out.println("R1:");
    for(int[] a:matrices.get(1))
    {
        for(int b:a)
        {
            System.out.print(b+" ");
        }
        System.out.println();//换行
    }
    System.out.println("R2:");
    for(int[] a:matrices.get(2))
    {
        for(int b:a)
        {
            System.out.print(b+" ");
        }
        System.out.println();//换行
    }
    System.out.println("R3:");
    for(int[] a:matrices.get(3))
    {
        for(int b:a)
        {
            System.out.print(b+" ");
        }
        System.out.println();//换行
    }
        Vector<int[]> vectors=new Vector();
        vectors.add(RNG1.yForming(matrices.get(1),matrices.get(1).length));
        vectors.add(RNG1.xpForming(matrices.get(1),matrices.get(1).length));
        vectors.add(RNG1.yForming(matrices.get(2),matrices.get(2).length));
        vectors.add(RNG1.xpForming(matrices.get(3),matrices.get(3).length));
        vectors.add(RNG1.yForming(matrices.get(3),matrices.get(3).length));

        Element[] ey1=new Element[vectors.get(1).length];
        ey1=RNG1.vectorEncrypt(vectors.get(1),BGNPK);
        double t2=System.currentTimeMillis();
        double t3=t2-t1;
        System.out.println("Query generated."+t3+" ms passed.");



    }

}
