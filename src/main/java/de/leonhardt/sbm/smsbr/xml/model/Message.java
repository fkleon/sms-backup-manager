package de.leonhardt.sbm.smsbr.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.leonhardt.sbm.smsbr.xml.NullLongAdapter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({Sms.class, Mms.class})
@Getter @Setter
public abstract class Message {

    /**
     * The Java date representation (including millisecond) of the time when the message was sent/received.
     */
    @XmlAttribute(name = "date", required = true)
    @XmlSchemaType(name = "unsignedLong")
    @XmlJavaTypeAdapter(NullLongAdapter.class)
    @NonNull
    protected Long date;

    /**
     * 0 if not sent
     */
    @XmlAttribute(name = "date_sent")
    @XmlSchemaType(name = "unsignedLong")
    @XmlJavaTypeAdapter(NullLongAdapter.class)
    @NonNull
    protected Long dateSent;

    /**
     * Read Message = 1, Unread Message = 0.
     */
    @XmlAttribute(name = "read", required = true)
    @XmlSchemaType(name = "unsignedByte")
    protected short read;

    /**
     * Optional field that has the date in a human readable format.
     */
    @XmlAttribute(name = "readable_date")
    protected String readableDate;

    /**
     * Optional field that has the name of the contact.
     */
    @XmlAttribute(name = "contact_name")
    protected String contactName;
}
