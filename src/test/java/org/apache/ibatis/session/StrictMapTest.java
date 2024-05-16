package org.apache.ibatis.session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author zhi.li
 * @create_time 2024/4/16
 * @description
 */
public class StrictMapTest {


  @Test
  void test() {
    StrictMap2<String> strictMap = new StrictMap2<String>("test")
      .conflictMessageProducer((savedValue, targetValue) -> "," + savedValue + " is already in use" + "，targetValue is " + targetValue);
    strictMap.put("key1", "key1");
    strictMap.put("key1.key0", "key1.key0");
    strictMap.put("key2.key0", "key2.key0");

    Assertions.assertEquals(4, strictMap.size());
    Assertions.assertEquals("key1", strictMap.get("key1"));
    Assertions.assertEquals("key1.key0", strictMap.get("key1.key0"));
    Assertions.assertEquals("key2.key0", strictMap.get("key2.key0"));

    //key1.key0  和key2.key0有冲突
    Assertions.assertThrows(IllegalArgumentException.class, () -> strictMap.get("key0"));

    //再次添加key1 会抛出异常
    Assertions.assertThrows(IllegalArgumentException.class, () -> strictMap.put("key1", "key1"));
    //获取一个不存在的key 也会抛异常
    Assertions.assertThrows(IllegalArgumentException.class, () -> strictMap.get("key3"));


  }


  protected static class StrictMap2<V> extends HashMap<String, V> {

    private static final long serialVersionUID = -4950446264854982944L;
    private final String name;
    private BiFunction<V, V, String> conflictMessageProducer;

    public StrictMap2(String name, int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
      this.name = name;
    }

    public StrictMap2(String name, int initialCapacity) {
      super(initialCapacity);
      this.name = name;
    }

    public StrictMap2(String name) {
      super();
      this.name = name;
    }

    public StrictMap2(String name, Map<String, ? extends V> m) {
      super(m);
      this.name = name;
    }

    /**
     * Assign a function for producing a conflict error message when contains value with the same key.
     * <p>
     * function arguments are 1st is saved value and 2nd is target value.
     *
     * @param conflictMessageProducer A function for producing a conflict error message
     * @return a conflict error message
     * @since 3.5.0
     */
    public StrictMap2<V> conflictMessageProducer(BiFunction<V, V, String> conflictMessageProducer) {
      this.conflictMessageProducer = conflictMessageProducer;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(String key, V value) {
      if (containsKey(key)) {
        throw new IllegalArgumentException(name + " already contains value for " + key
          + (conflictMessageProducer == null ? "" : conflictMessageProducer.apply(super.get(key), value)));
      }
      if (key.contains(".")) {
        final String shortKey = getShortName(key);
        if (super.get(shortKey) == null) {
          super.put(shortKey, value);
        } else {
          super.put(shortKey, (V) new Ambiguity(shortKey));
        }
      }
      return super.put(key, value);
    }

    @Override
    public V get(Object key) {
      V value = super.get(key);
      if (value == null) {
        throw new IllegalArgumentException(name + " does not contain value for " + key);
      }
      if (value instanceof Ambiguity) {
        throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
          + " (try using the full name including the namespace, or rename one of the entries)");
      }
      return value;
    }

    protected static class Ambiguity {
      private final String subject;

      public Ambiguity(String subject) {
        this.subject = subject;
      }

      public String getSubject() {
        return subject;
      }
    }

    private String getShortName(String key) {
      final String[] keyParts = key.split("\\.");
      return keyParts[keyParts.length - 1];
    }
  }
}
