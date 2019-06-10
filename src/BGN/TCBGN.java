package BGN;

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

public class TCBGN {

    public static final String start = "start";
    public static final String end = "end";
    private PairingParameters param;
    private BigInteger p;
    private BigInteger q; // This is the private key.
    private BigInteger h;
    private BigInteger order;

    public PublicKey gen (int K)
    {
        double t3=System.currentTimeMillis();
        TypeA1CurveGenerator a1 = new TypeA1CurveGenerator( 2, K);
        param = a1.generate();
        TypeA1Pairing pairing = new TypeA1Pairing(param);
        System.out.println(param);
        double t4=System.currentTimeMillis();
        System.out.println("Generated a new paring. "+(t4-t3)+" ms passed.");

        order = param.getBigInteger("n"); //n为阶，存在关系order=pq
        p = param.getBigInteger("n0"); //n0为生成的第一个随机数
        q = param.getBigInteger("n1"); //n1为生成的第二个随机数

        Field f = pairing.getG1();
        Element P = f.newRandomElement();
        P = P.mul(param.getBigInteger("l")); //P is a generator g of G
        Element Q = f.newElement();
        Q = Q.set(P);
        Q = Q.pow(q);//Q represents h,h=g^q
        return new PublicKey(pairing, P, Q, order,p); //P is g, Q is h, pairing is e
    }
    public Element Encrypt(PublicKey PK,int msg){

        System.out.println("【Start to encrypt "+msg+"】");
        SecureRandom randomGen = new SecureRandom();
        double t5=System.currentTimeMillis();
        Field f=PK.getField();
        Element C= f.newRandomElement();
        Element g= PK.getP().duplicate();
        Element h=PK.getQ().duplicate();
        BigInteger r = BigInteger.valueOf(randomGen.nextInt(512));
        C=C.set(g);
        C=C.pow(BigInteger.valueOf(msg));
        h=h.pow(r);
        C=C.mul(h);
        double t6=System.currentTimeMillis();
        System.out.println("Random factor r="+r);
        System.out.println("C="+C);
        System.out.println("【 "+msg+" Encrypted,"+(t6-t5)+" ms passed.】\n");
        return C;

    }
    public BigInteger Decrypt(PublicKey PK,Element C)
    {
        BigInteger m=(BigInteger.valueOf(1));
        System.out.println("【Start to Decrypt.】");
        double t7=System.currentTimeMillis();
        Field F = PK.getField();
        BigInteger sk =p;
        Element g =PK.getP();
        Element gp =g.pow(sk);
        Element C2=C.pow(p);
        Element test=gp.duplicate();
        while (!test.isEqual(C2)) {
            // This is a brute force implementation of finding the discrete
            // logarithm.
            // Performance may be improved using algorithms such as Pollard's
            // Kangaroo.
            test=test.mul(gp);
            m = m.add(BigInteger.valueOf(1));
        }
        double t8=System.currentTimeMillis();
        System.out.println("Decrypted message ="+m+", "+(t8-t7)+" ms passed.\n");
        return m;
    }

    public Element GGTadd(PublicKey PK,Element A,Element B,Element E1)
    {
        A=A.mul(B);
        Element C=PK.doPairing(A,E1);
        return C;
    }
    public Element GGTInit(PublicKey PK,Element C,Element E1)
    {
        C=PK.doPairing(C,E1);
        return C;
    }
    public Element GGTmul(PublicKey PK,Element A,BigInteger B)
    {
        Element C=A.pow(B);
        return C;
    }
    public Element GTOGT(PublicKey PK,Element A,Element B)
    {
        Element C=PK.doPairing(A,B);
        return C;
    }
    public BigInteger GGTDecrypt(PublicKey PK,Element C)
    {
        System.out.println("【Start to GGT Decrypt.】");
        double t9=System.currentTimeMillis();
        BigInteger msg=BigInteger.ONE;
        BigInteger sk=p;
        Element C2=C.pow(sk);
        Element g =PK.getP();
        Element EGGP=PK.doPairing(g,g);
        EGGP=EGGP.pow(sk);
        Element testGGT=EGGP.duplicate();
        while (!testGGT.isEqual(C2)) {
            // This is a brute force implementation of finding the discrete
            // logarithm.
            // Performance may be improved using algorithms such as Pollard's
            // Kangaroo.
            testGGT=testGGT.mul(EGGP);
            //System.out.println("Testing msg ="+msg+",testGGT="+testGGT);  Enable this to know the valve of every step
            msg = msg.add(BigInteger.valueOf(1));

        }
        double t10=System.currentTimeMillis();
        System.out.println("GGT Decrypted completed in "+(t10-t9)+"ms. message ="+msg);
        return msg;
    }


    public static void main(String[] args) {
        TCBGN b = new TCBGN();
        PublicKey BGNPK=b.gen(512);
        Field f=BGNPK.getField();
        Element C=b.Encrypt(BGNPK,50);
        BigInteger m=b.Decrypt(BGNPK,C);
        Element B =b.Encrypt(BGNPK,10).getImmutable();
        Element E =b.Encrypt(BGNPK,1).getImmutable();  //Pre-calculate a "E(1)"to convert elements in G to GT
        C=BGNPK.doPairing(B,C).getImmutable();
        Element D=b.Encrypt(BGNPK,10);
        D=b.GTOGT(BGNPK,D,B);
        D=b.GGTmul(BGNPK,D,BigInteger.valueOf(5));
        Element F=b.Encrypt(BGNPK,3).getImmutable();
        BigInteger m2=b.GGTDecrypt(BGNPK,D);

    }



}
