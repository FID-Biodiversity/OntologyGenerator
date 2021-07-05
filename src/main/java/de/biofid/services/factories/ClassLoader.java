package de.biofid.services.factories;

import de.biofid.services.exceptions.ValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLoader {

    protected static final Logger logger = LogManager.getLogger();

    public static Object createInstanceOfClassFromName(String className) throws ValueException {
        Constructor<?> constructor = getClassConstructorFromName(className);
        Object instantiatedObject = instantiateObjectFromConstructor(constructor);

        if (instantiatedObject == null) {
            throw new ValueException("The given class name " + className + " or its constructor, taking no parameters, " +
                    "could not be found and instantiated!");
        }

        return instantiatedObject;
    }

    private static Constructor<?> getClassConstructorFromName(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getConstructor();
        } catch (ClassNotFoundException e) {
            logger.warn("The given class \"" + className + "\" could not be found!");
        } catch (NoSuchMethodException e) {
            logger.warn("The given class \"" + className + "\" does not have an empty constructor!");
        }
        return null;
    }

    private static Object instantiateObjectFromConstructor(Constructor<?> constructor) {
        if (constructor == null) {
            return null;
        }

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
           logger.warn("Could not instantiate an object for the class " + constructor.getClass().getName() + "!");
           return null;
        }
    }
}
