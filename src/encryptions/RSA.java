package encryptions;

import java.math.BigInteger;
import java.util.Random;

public class RSA{
	private BigInteger p, n, q, phi, e, d;
	
	public BigInteger encrypt(BigInteger m){
		if(n == null){
			generateKeys();
		}
		return repeatedSquareAndMultiply(m, e, n);
	}
	
	public BigInteger decrypt(BigInteger c){
		if(d==null){
			generateKeys();
		}
		return repeatedSquareAndMultiply(c, d, n);
	}
	
	/**
	 * 
	 * @param min
	 *            inclusive minimum value
	 * @param max
	 *            inclusive maximum value
	 * @return a BigInteger in the range
	 */
	private BigInteger nextRandomBigInteger(BigInteger min, BigInteger max){
		Random rand = new Random();
		BigInteger result = new BigInteger(max.bitLength(),rand);
		while(result.compareTo(max) > 0 || result.compareTo(min) < 0){
			result = new BigInteger(max.bitLength(),rand);
		}
		return result;
	}
	
	/**
	 * 
	 * @param n
	 *            odd integer greater than 2
	 * @param t
	 *            security parameter
	 * @return true if n is prime
	 */
	private boolean fermatPrimalityTest(BigInteger n, long t){
		for(int i = 0; i < t; i++){
			BigInteger a = nextRandomBigInteger(new BigInteger("2"),n.subtract(new BigInteger("2")));
			if(!repeatedSquareAndMultiply(a,n.subtract(BigInteger.ONE),n).equals(BigInteger.ONE)){
				return false;
			}
		}
		return true;
	}
	/*@formatter:off
	private long repeatedSquareAndMultiply(long a, long k){
		long b=1;
		if(k==0){
			return b;
		}
		long A=a;
		if(k%2==1){
			b=a;
		}
		k/=2;
		while(k>0){
			A=(A*A)%n;
			if(k%2==1){
				b=(A*b)%n;
			}
			k/=2;
		}
		return b;
	}
	@formatter:on
	*/
	/*@formatter:off
	private long repeatedSquareAndMultiply(long a, long k, long n){
		long b = 1;
		while(k > 0){
			if(k % 2 == 1){
				b = (a * b) % n;
			}
			k /= 2;
			a = (a * a) % n;
		}
		return b;
	}
	@formatter:on*/
	private BigInteger repeatedSquareAndMultiply(BigInteger a, BigInteger k, BigInteger n){
		BigInteger b = BigInteger.ONE;
		while(k.compareTo(BigInteger.ZERO) > 0){
			if(k.mod(new BigInteger("2")).equals(BigInteger.ONE)){
				b = a.multiply(b).mod(n);
			}
			k= k.divide(new BigInteger("2"));
			a = a.multiply(a).mod(n);
		}
		return b;
	}
	
	private void extendedEuclidian(BigInteger e){
		BigInteger a = e, b = phi, x = BigInteger.ZERO,
				y = BigInteger.ONE, lastx = BigInteger.ONE,
				lasty = BigInteger.ZERO;
		while(!b.equals(BigInteger.ZERO)){
			BigInteger temp = x;
			x = lastx.subtract(a.divide(b).multiply(x));
			lastx = temp;
			
			temp = y;
			y = lasty.subtract(a.divide(b).multiply(y));
			lasty = temp;
			
			BigInteger remainder = a.mod(b);
			a = b;
			b = remainder;
		}
		//e=originalA
		//phi=originalB
		//d=lastx
		//gcd(e,phi)=a
		if(a.equals(BigInteger.ONE)){
			this.e = e;
			this.d = lastx;
			while(d.compareTo(BigInteger.ZERO) < 0){
				d=d.add(phi);
			}
			while(d.compareTo(phi) >= 0){
				d=d.subtract(phi);
			}
		}
	}
	
	public BigInteger encrypt(BigInteger m, BigInteger n, BigInteger e){
		this.n=n;
		this.e=e;
		return encrypt(m);
	}
	
	public void generateKeys(){
		while(p==null){
			p=nextRandomBigInteger(new BigInteger("99999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999999999999999999999999999999999"));
			if(!fermatPrimalityTest(p,50)){
				p=null;
			}
		}
		while(q==null){
			q=nextRandomBigInteger(new BigInteger("99999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999999999999999999999999999999999"));
			if(!fermatPrimalityTest(q,50)){
				q=null;
			}
		}
		
		n=p.multiply(q);
		phi=p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		while(d==null){
			e=nextRandomBigInteger(new BigInteger("2"), phi.subtract(BigInteger.ONE));
			extendedEuclidian(e);
		}
	}
	
	public void printKeys(){
		System.out.println("N="+n);
		System.out.println("p="+p);
		System.out.println("q="+q);
		System.out.println("phi="+phi);
		System.out.println("e="+e);
		System.out.println("d="+d);
	}
	
	public static void main(String[] args){
		RSA bob = new RSA();
		RSA alice = new RSA();
		
		//step 1: bob makes keys
		bob.generateKeys();
		bob.printKeys();
		
		//step 2: alice gets public keys from bob
		alice.setPublicKeys(bob.getPublicKeys());
		
		//step 3: alice encrypts the message
		BigInteger c = alice.encrypt(new BigInteger("1165161641"));
		System.out.println(c);
		
		//step 4: bob decrypts the message
		System.out.println(bob.decrypt(c));
	}

	private void setPublicKeys(BigInteger[] publicKeys){
		this.n=publicKeys[0];
		this.e=publicKeys[1];
	}

	/**
	 * 
	 * @return [n,e]
	 */
	private BigInteger[] getPublicKeys(){
		if(n==null){
			generateKeys();
		}
		return new BigInteger[] {n,e};
	}
}
