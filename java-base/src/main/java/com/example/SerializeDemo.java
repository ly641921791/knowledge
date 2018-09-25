package com.example;

import lombok.Data;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @see #testA() 不实现序列化接口不能序列化
 * @see #testB() 实现序列化接口可以序列化
 * @see #testC() 读取序列化数据成Object，且静态字段不能读，transient（短暂、临时）修饰的字段也会忽视序列化
 * @see #testD() 通过writeObject和readObject方法自定义序列化规则，可以通过该方式加密字段
 */
public class SerializeDemo {

	@Test
	public void testA() {
		try (
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)
		) {
			A a = new A();
			a.setId(1);
			oos.writeObject(a);
			System.out.println(bos.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testB() {
		try (
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)
		) {
			B b = new B();
			b.setId(1);
			oos.writeObject(b);
			System.out.println(bos.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testC() {
		try (
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
		) {
			B b = new B();
			b.setId(1);
			b.setName("ly");
			oos.writeObject(b);

			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			// 先设置静态字段为2，都读取，最终打印出2说明序列化时，原有的1并没有写入和读出
			b.STATIC_ID = 2;

			B o = (B) ois.readObject();
			System.out.println(o);
			System.out.println(o.STATIC_ID);

			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testD() {
		try (
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
		) {
			C c = new C();
			c.setId(1);
			oos.writeObject(c);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			C o = (C) ois.readObject();
			System.out.println(o);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

@Data
class A {
	private int id;
}

@Data
class B implements Serializable {

	private static final long serialVersionUID = 1L;

	public static int STATIC_ID = 1;

	private int id;

	private transient String name;
}

@Data
class C implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private void writeObject(ObjectOutputStream out) throws IOException {
		System.out.println("writeObject");
		ObjectOutputStream.PutField putField = out.putFields();
		putField.put("id", id);
		out.writeFields();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		System.out.println("readObject");
		ObjectInputStream.GetField getField = in.readFields();
		id = getField.get("id", 0);
	}
}