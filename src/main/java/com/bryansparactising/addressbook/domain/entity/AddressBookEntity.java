package com.bryansparactising.addressbook.domain.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "addressbook")
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "uuid", length = 36)
    private String uuid;

    @ManyToMany()
    @JoinTable(
        name = "contact_addressbook",
        joinColumns = @JoinColumn(name = "addressbook_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<ContactEntity> contacts = new HashSet<>();

    public void addContact(ContactEntity contact) {
        this.contacts.add(contact);
        contact.getAddressBooks().add(this); // Update the inverse side
    }

    public void removeContact(ContactEntity contact) {
        this.contacts.remove(contact);
        contact.getAddressBooks().remove(this); // Update the inverse side
    }
}
