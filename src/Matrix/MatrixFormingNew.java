package Matrix;

import BGN.PublicKey;
import BGN.TCBGN;

import it.unisa.dia.gas.jpbc.Element;
import java.util.Vector;
import java.io.*;

public class MatrixFormingNew {
    public int[] Bottom=new int[10];
    public int[] Top=new int[10];
    int[] pstart=new int[10];
    int[] length=new int[10];
    int pend=0;
    int[] shift =new int[10];

    public int[] getBottom() {
        return Bottom;
    }

    public int[] getPstart() {
        return pstart;
    }

    public int[] getLength() {
        return length;
    }

    public int[] getTop() {
        return Top;
    }

    public int getPend() {
        return pend;
    }

    public int[] getShift() {
        return shift;
    }

    public int matrixOrderCalc(int[] Bottom, int[] Top, MatrixFormingNew MFNew)
    {
        int matrixOrder=0;
        int[] pstart=new int[10];
        int[] length=new int[10];
        int[] shift=new int[10];
        int pend=0;
        for(int i=0;Bottom[i]!=0;i++)
        {
            if (i==0)
                pstart[i]=0;
            else
                pstart[i]=pend+length[i-1];
            shift[i]=pstart[i]-Bottom[i]+1;
            length[i]=Top[i]-Bottom[i]+1;
            pend=pstart[i]+length[i];
        }
        MFNew.pstart=pstart;
        MFNew.length=length;
        MFNew.pend=pend;
        MFNew.shift=shift;
        return pend;
    }
    public  int[][] matrixFormingNew(MatrixFormingNew MFNew)
    {
        int[] length = MFNew.getLength();
        int[] pstart = MFNew.getPstart();
        int matrixOrder=1;
        int j=0;
        int i=0;
        int k=0;
        int rangeLength=0;
        while(MFNew.getPend()>1)
        {
            if((i*i)-MFNew.getPend()<0)
                i++;
            else
                break;
        }
        matrixOrder=i;
        int[][] matrix = new int[matrixOrder][matrixOrder];
        for(k=0;length[k]!=0;k++)
        {
            i=pstart[k]/matrixOrder;
            j=pstart[k]%matrixOrder;
            rangeLength=length[k];
            while(rangeLength!=0)
            {
                matrix[i][j]=1;
                j++;
                if (j>=matrixOrder)
                {
                    j=0;
                    i++;
                }
                rangeLength--;
            }
        }
        return matrix;
    }
    public Vector<int[][]> prMatrixForming(int[][] oriMatrix)
    {
        Vector<int[][]> prMatrices = new Vector<>(oriMatrix.length);
        int i=0;
        int j=0;
        int prFlag=0;
        for(i=0;i<oriMatrix.length;i++)
        {
            int[][] matrixn=new int[oriMatrix.length][oriMatrix.length];
            prFlag=0;
            for (j=0;j<oriMatrix.length;j++)
            {
                if(oriMatrix[i][j]==1)
                {
                    prFlag+=1;
                }
            }
            if (prFlag>0 && prFlag<oriMatrix.length)
            {
                for (j=0;j<oriMatrix.length;j++)
                {
                    if(oriMatrix[i][j]==1)
                    {
                        matrixn[i][j]=1;
                    }
                }
                prMatrices.add(matrixn);
            }
        }
        return prMatrices;
    }
    public int[][] crMatrixForming(int[][] oriMatrix)
    {
        int[][] crMatrix = new int[oriMatrix.length][oriMatrix.length];
        int i=0;
        int j=0;
        int crFlag=0;
        for(i=0;i<oriMatrix.length;i++)
        {
            crFlag=0;
            for (j=0;j<oriMatrix.length;j++)
            {
                if(oriMatrix[i][j]==1)
                {
                    crFlag+=1;
                }
            }
            if (crFlag==oriMatrix.length)
            {
                for (j=0;j<oriMatrix.length;j++)
                {
                    if(oriMatrix[i][j]==1)
                    {
                        crMatrix[i][j]=1;
                    }
                }
            }
        }
        return crMatrix;
    }
    public int[] yVectorForming(int[][] oriMatrix)
    {
        int[] yVector = new int[oriMatrix.length];
        int i,j=0;
        for(j=0;j<oriMatrix.length;j++)
        {
            yVector[j]=1;
            for (i=0;i<oriMatrix.length;i++)
            {
                if (oriMatrix[i][j]==1)
                {
                    yVector[j]=0;
                    break;
                }
            }
        }
        return yVector;
    }
    public int[] xpVectorForming(int[][] oriMatrix)
    {
        int[] xpVector= new int[oriMatrix.length];
        int i,j=0;
        for(i=0;i<oriMatrix.length;i++)
        {
            xpVector[i]=1;
            for (j=0;j<oriMatrix.length;j++)
            {
                if (oriMatrix[i][j]==1)
                {
                    xpVector[i]=0;
                    break;
                }
            }
        }
        return xpVector;
    }
    public Vector<int[]> yAllForming(Vector<int[][]> prMatrices)
    {
        Vector<int[]> yAll=new Vector<>(prMatrices.size()*2);
        int i=0;
        for(i=0;i<prMatrices.size();i++)
        {
            yAll.add(yVectorForming(prMatrices.get(i)));
            yAll.add(xpVectorForming(prMatrices.get(i)));
        }
        return yAll;
    }
    public Element[] vectorEncrypt(int[] oriVector,PublicKey BGNPK)
    {
        Element[] encVector=new Element[oriVector.length];
        int i=0;
        TCBGN BGN=new TCBGN();
        for(i=0;i<oriVector.length;i++)
        {
            encVector[i]=BGN.Encrypt(BGNPK,oriVector[i]);
        }
        return encVector;
    }
    public static void main(String[] args){
        MatrixFormingNew MFNew=new MatrixFormingNew();
        int[] Btm={10,15,20,0,0,0,0,0,0,0};
        int[] Top={40,45,50,0,0,0,0,0,0,0};
        //Top[1]=45;
        //Top[2]=50;
        double t1=0,t2=0,t3=0;
        TCBGN BGN =new TCBGN();
        PublicKey BGNPK=BGN.gen(512);
        t1=System.currentTimeMillis();
        System.out.println(MFNew.matrixOrderCalc(Btm,Top,MFNew));
        int[][] matrix=MFNew.matrixFormingNew(MFNew);

        System.out.println("R:");
        for(int[] a:matrix)
        {
            for(int b:a)
            {
                System.out.print(b+" ");
            }
            System.out.println();//换行
        }
        Vector<int[][]> prMatrices= MFNew.prMatrixForming(matrix);
        System.out.println("R1:");
        for(int[] a:prMatrices.get(0))
        {
            for (int b : a) {
                System.out.print(b + " ");
            }
            System.out.println();//换行
        }
            int[][] crMatrix =MFNew.crMatrixForming(matrix);
            System.out.println("RC:");
            for(int[] a:crMatrix)
            {
                for(int b:a)
                {
                    System.out.print(b+" ");
                }
                System.out.println();//换行
        }
            Vector<int[]> yAll=MFNew.yAllForming(prMatrices);
            yAll.add(MFNew.xpVectorForming(crMatrix));
            Element[] yAllEnc=MFNew.vectorEncrypt(yAll.get(0),BGNPK);
            t2=System.currentTimeMillis();
            t3=t2-t1;
            System.out.println("Query generated. "+t3+" ms passed.");
            //System.out.println("Single unit length:"+yAllEnc[1].getLengthInBytes());
    }
}
