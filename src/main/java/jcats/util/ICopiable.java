package jcats.util;

public interface ICopiable<T> {
	public void copyFrom(T t);
	public T clone();
}
