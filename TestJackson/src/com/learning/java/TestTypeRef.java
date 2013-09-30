package com.learning.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class TypeRef<T> {
	private final Type type;
	protected TypeRef() {
		type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		System.out.println("Typeref is refering to object of" + type);
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof TypeRef && ((TypeRef)o).type.equals(type);
	}
	
	@Override
	public int hashCode() {
		return type.hashCode();
	}
}

public class TestTypeRef {
	public static <T> void getType(TypeRef<T> t) {
		ParameterizedType p = (ParameterizedType) t.getClass()
				.getGenericSuperclass();
		System.out.println(t.getClass());
		System.out.println(p);
		System.out.println(p.getActualTypeArguments()[0]);
	}
	
	public static <T> void testGernericList() {
		Map<TypeRef<?>, Object> objects = new HashMap<TypeRef<?>, Object>();
		TypeRef<ArrayList<T>> ref = new TypeRef<ArrayList<T>>(){};
		objects.put(ref, new ArrayList<String>());
		
		@SuppressWarnings("unchecked")
		ArrayList<String> ls = (ArrayList<String>)objects.get(new TypeRef<ArrayList<T>>(){});
		ls.add("ss");
		System.out.println(ls.get(0));
		@SuppressWarnings("unchecked")
		ArrayList<Integer> lis = (ArrayList<Integer>)objects.get(new TypeRef<ArrayList<T>>(){});
		lis.add(1);
		for(String s : ls) {
			System.out.println(s);
		}
	}
	
	public static void test() {
		//Test for generic type erasure
		ArrayList<Integer> intArr = new ArrayList<Integer>();
		ArrayList<Float> floatArr = new ArrayList<Float>();
		if(intArr.getClass() == floatArr.getClass())
			System.out.println("generic type erasure is taking effect.");
		System.out.println(intArr.getClass());
		System.out.println(intArr.getClass().getGenericSuperclass());
		getType(new TypeRef<ArrayList<Integer>>(){});
		testGernericList();
		class a extends ArrayList<String> {};
		System.out.println(a.class.getGenericSuperclass());
	}
}