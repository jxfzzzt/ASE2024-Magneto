package com.magneto.fuzz;

import com.magneto.fuzz.instrument.FuzzClassTransformer;
import com.magneto.util.ClassUtil;
import com.magneto.util.IOUtil;
import com.magneto.util.URLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.List;

public class FuzzClassLoader extends URLClassLoader {

    public static final String STATE_TABLE_CLASS_NAME = "com.magneto.instrument.state.GlobalStateTable";

    public static final String STATE_TABLE_INTERNAL_NAME = "com/magneto/instrument/state/GlobalStateTable";

    public static final String STATE_NODE_CLASS_NAME = "com.magneto.instrument.state.StateNode";

    public static final String STATE_NODE_INTERNAL_NAME = "com/magneto/instrument/state/StateNode";

    public static final String ADD_STATE_NODE_METHOD_NAME = "addStateNode";

    public static final String ADD_METHOD_SIGNATURE = "addMethodSignature";

    private static final byte[] STATE_TABLE_BYTES;

    private static final byte[] STATE_NODE_BYTES;

    private ClassLoader testcaseLoader = null;


    static {
        ClassLoader loader = getSystemClassLoader();
        try {
            STATE_TABLE_BYTES = ClassUtil.getClassBytes(loader, STATE_TABLE_CLASS_NAME);

            STATE_NODE_BYTES = ClassUtil.getClassBytes(loader, STATE_NODE_CLASS_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final FuzzClassTransformer transformer = new FuzzClassTransformer();

    public FuzzClassLoader(List<String> pathList) throws MalformedURLException {
        super(URLUtil.stringsToUrls(pathList.toArray(new String[0])), ClassLoader.getSystemClassLoader().getParent()); // break the parent delegation
    }

    public void setTestcaseLoader(ClassLoader testcaseLoader) {
        this.testcaseLoader = testcaseLoader;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (name == null) return null;

        if (name.startsWith("org.junit") || name.startsWith("junit")) { // Junit package is loaded by the Application ClassLoader
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }

        if (STATE_TABLE_CLASS_NAME.equals(name)) {
            return defineClass(name, STATE_TABLE_BYTES, 0, STATE_TABLE_BYTES.length);
        } else if (STATE_NODE_CLASS_NAME.equals(name)) {
            return defineClass(name, STATE_NODE_BYTES, 0, STATE_NODE_BYTES.length);
        } else {
            String internalName = name.replace('.', '/');
            String path = internalName.concat(".class");
            byte[] originalBytecode;

            try {
                InputStream in = super.getResourceAsStream(path);
                if (in == null && testcaseLoader != null) {
                    in = testcaseLoader.getResourceAsStream(path);
                }

                if (in == null) {
                    throw new ClassNotFoundException("Cannot find class " + name);
                }

                originalBytecode = IOUtil.readBytes(in);
            } catch (IOException e) {
                throw new ClassNotFoundException("I/O exception while loading class.", e);
            }

            assert (originalBytecode != null);

            byte[] bytesToLoad;
            try {
                byte[] instrumented = transformer.transform(this, internalName, null, null, originalBytecode);
                if (instrumented != null) {
                    bytesToLoad = instrumented;
                } else {
                    bytesToLoad = originalBytecode;
                }
            } catch (IllegalClassFormatException e) {
                bytesToLoad = originalBytecode;
            }

            return defineClass(name, bytesToLoad, 0, bytesToLoad.length);
        }
    }

}
