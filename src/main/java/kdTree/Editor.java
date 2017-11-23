package kdTree;
//从kd树中增加或者删除节点

public interface Editor<T> {
    public T edit(T current) throws KeyDuplicateException;

    public static abstract class BaseEditor<T> implements Editor<T> {
        final T val;
        public BaseEditor(T val) {
            this.val = val;
        }
        public abstract T edit(T current) throws KeyDuplicateException;
    }
    public static class Inserter<T> extends BaseEditor<T> {
        public Inserter(T val) {
            super(val);
        }
        public T edit(T current) throws KeyDuplicateException {
            if (current == null) {
                return this.val;
            }
            throw new KeyDuplicateException();
        }
    }
    public static class OptionalInserter<T> extends BaseEditor<T> {
        public OptionalInserter(T val) {
            super(val);
        }
        public T edit(T current) {
            return (current == null) ? this.val : current;
        }
    }
    public static class Replacer<T> extends BaseEditor<T> {
        public Replacer(T val) {
            super(val);
        }
        public T edit(T current) {
            return this.val;
        }
    }
}
