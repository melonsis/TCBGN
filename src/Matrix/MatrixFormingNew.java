package Matrix;

import java.util.Vector;

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
    public Vector prMatrixForming(int[][] oriMatrix)
    {
        Vector<int[][]> prMatrices = new Vector<>(oriMatrix.length);
        int i=0;
        int j=0;
        int prFlag=0;
        int [][] matrixn=new int[oriMatrix.length][oriMatrix.length];
        for(i=0;i<oriMatrix.length;i++)
        {
            matrixn=new int[oriMatrix.length][oriMatrix.length];
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
    public static void main(String[] args){
        MatrixFormingNew MFNew=new MatrixFormingNew();
        int[] Btm=new int[10];
        Btm[0]=10;
        Btm[1]=15;
        Btm[2]=20;
        int[] Top=new int[10];
        Top[0]=25;
        Top[1]=27;
        Top[2]=30;

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
        for(int[] a:prMatrices.get(1))
        {
            for(int b:a)
            {
                System.out.print(b+" ");
            }
            System.out.println();//换行
        }
    }
}
