package it.algos.webbase.web.pref;

import com.google.common.primitives.Longs;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibImage;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;


/**
 * Classe astratta di supporto per mantenere dati e
 * metodi comuni per la gestione delle preferenze
 */
public abstract class AbsPref {


    /**
     * Converte un valore Object in ByteArray per questa preferenza.
     * <p>
     *
     * @param obj il valore Object
     * @return il valore convertito in byte[]
     * @deprecated
     */
    public static byte[] objectToBytes(PrefIF pref, Object obj) {
        byte[] bytes = new byte[0];
        if (obj != null) {
            switch (pref.getType()) {
                case bool:
                    if (obj instanceof Boolean) {
                        boolean bool = (boolean) obj;
                        bytes = new byte[]{(byte) (bool ? 1 : 0)};
                    }
                    break;
                case bytes:
                    if (obj instanceof byte[]) {
                        bytes = (byte[]) obj;
                    }
                    break;
                case date:
                    if (obj instanceof Date) {
                        Date date = (Date) obj;
                        long millis = date.getTime();
                        bytes = Longs.toByteArray(millis);
                    }
                    break;
                case decimal:
                    if (obj instanceof BigDecimal) {
                        BigDecimal bd = (BigDecimal) obj;
                        bytes = bigDecimalToByteArray(bd);
                    }

                    break;
                case integer:
                    if (obj instanceof Integer) {
                        int num = (Integer) obj;
                        bytes = intToByteArray(num);
                    }
                    break;
                case string:
                    if (obj instanceof String) {
                        String string = (String) obj;
                        bytes = string.getBytes(Charset.forName("UTF-8"));
                    }
                    break;
            }
        }
        return bytes;
    }

    /**
     * Converte un byte[] in Object del tipo adatto per questa preferenza.
     * <p>
     *
     * @param obj il valore come byte[]
     * @return il valore convertito nell'oggetto del tipo adeguato
     * @deprecated
     */
    public static Object bytesToObject(PrefIF pref, byte[] bytes) {
        Object obj = null;
        if (bytes != null) {
            switch (pref.getType()) {
                case bool:
                    if (bytes.length > 0) {
                        byte b = bytes[0];
                        obj = new Boolean(b == (byte) 0b00000001);
                    } else {
                        obj = new Boolean(false);
                    }
                    break;
                case bytes:
                    obj = bytes;
                    break;
                case date:
                    long millis = Longs.fromByteArray(bytes);
                    obj = new Date(millis);
                    break;
                case decimal:
                    obj = byteArrayToBigDecimal(bytes);
                    break;
                case integer:
                    obj = byteArrayToInt(bytes);
                    break;
                case string:
                    obj = new String(bytes, Charset.forName("UTF-8"));
                    break;
            }
        }

        return obj;
    }


    /**
     * @deprecated
     */
    private static byte[] bigDecimalToByteArray(BigDecimal bd) {
        double value = bd.doubleValue();
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    /**
     * @deprecated
     */
    private static BigDecimal byteArrayToBigDecimal(byte[] bytes) {
        double dbl = ByteBuffer.wrap(bytes).getDouble();
        return new BigDecimal(dbl);
    }

    /**
     * @deprecated
     */
    private static int byteArrayToInt(byte[] b) {
        int num = 0;
        if ((b != null) && (b.length > 0)) {
            num = b[3] & 0xFF |
                    (b[2] & 0xFF) << 8 |
                    (b[1] & 0xFF) << 16 |
                    (b[0] & 0xFF) << 24;
        }
        return num;
    }

    /**
     * @deprecated
     */
    private static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * Writes a value in the storage for  a given preference
     * <p>
     * If the preference does not exist it is created now.
     *
     * @param the   code for the preference
     * @param value the value
     */
    public static void put(PrefIF pref, Object value) {
        PrefEntity entity = getPreference(pref);
        if (entity == null) {
            entity = new PrefEntity();
            entity.setCode(pref.getCode());
        }
        entity.setValue(objectToBytes(pref, value));

        EntityManager manager = EM.createEntityManager();
        manager.getTransaction().begin();
        try {
            if (entity.getId() != null) {
                manager.merge(entity);
            } else {
                manager.persist(entity);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

        manager.close();

    }


    /**
     * Retrieves the entity for a given preference from the storage.
     * <p>
     *
     * @param pref - the preference
     * @return the entity, null if not found
     */
    public static PrefEntity getPreference(PrefIF pref) {
        String code = pref.getCode();
        PrefEntity entity = null;
        JPAContainer<?> container;

        EntityManager manager = EM.createEntityManager();
        container = JPAContainerFactory.makeNonCachedReadOnly(PrefEntity.class, manager);
        Filter filter = new Compare.Equal(PrefEntity_.code.getName(), code);
        container.addContainerFilter(filter);

        if (container.size() == 1) {
            Collection<Object> ids = container.getItemIds();
            Object id = ids.iterator().next();
            entity = (PrefEntity) container.getItem(id).getEntity();
        }

        manager.close();

        return entity;

    }


    /**
     * Removes  a given preference from the storage.
     * <p>
     *
     * @param pref - the preference
     * @param the  code for the preference
     */
    public static void remove(PrefIF pref) {
        PrefEntity entity = getPreference(pref);
        if (entity != null) {

            EntityManager manager = EM.createEntityManager();
            manager.getTransaction().begin();
            try {
                entity = manager.merge(entity);
                manager.remove(entity);
                manager.getTransaction().commit();
            } catch (Exception e) {
                manager.getTransaction().rollback();
            }

            manager.close();
        }
    }

    /**
     * Resets this preference to its default value .
     * <p>
     */
    public static void reset(PrefIF pref) {
        put(pref, pref.getDefaultValue());
    }

    /**
     * Retrieves the value for  a given preference from the storage
     * <p>
     * The returned value is already converted to the appropriate type.<br>
     * If the preference is missing from the storage, the default value is returned.<br>
     *
     * @return the value
     * <p>
     */
    public static Object get(PrefIF pref) {

        Object obj = null;
        PrefEntity entity = getPreference(pref);

        if (entity != null) {
            byte[] bytes = entity.getValue();
            obj = bytesToObject(pref, bytes);
        } else {
            obj = pref.getDefaultValue();
        }

        return obj;
    }


    /**
     * @deprecated
     */
    public static Image getImage(PrefIF pref) {
        Image img = null;
        byte[] bytes = (byte[]) get(pref);
        if (bytes.length > 0) {
            img = LibImage.getImage(bytes);
        }
        return img;
    }

    /**
     * @deprecated
     */
    public static Resource getResource(PrefIF pref) {
        Resource res = null;
        Image img = getImage(pref);
        if (img != null) {
            res = img.getSource();
        }
        return res;
    }


    /**
     * Checks if this preference exists in the storage.
     * <p>
     *
     * @return true if the preference exists
     */
    public static boolean exists(PrefIF pref) {
        return (getPreference(pref) != null);
    }


    public static boolean getBool(PrefIF pref) {
        return (boolean) get(pref);
    }

    public static byte[] getBytes(PrefIF pref) {
        return (byte[]) get(pref);
    }

    public static Date getDate(PrefIF pref) {
        return (Date) get(pref);
    }

    public static BigDecimal getDecimal(PrefIF pref) {
        return (BigDecimal) get(pref);
    }

    public static int getInt(PrefIF pref) {
        return (int) get(pref);
    }

    public static String getString(PrefIF pref) {
        return (String) get(pref);
    }

    /**
     * Enum dei tipi di preferenza supportati
     */
    public enum PrefType {
        string, bool, integer, decimal, date, bytes;
    }// end of internal enumeration


}
