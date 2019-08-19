package source.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeWrapper {

    private static Unsafe unsafe;

    public static Unsafe getUnsafe(){
        if(unsafe == null){
            try{
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                unsafe = (Unsafe) field.get(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return unsafe;
    }

}
