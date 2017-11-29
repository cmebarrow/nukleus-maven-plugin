# Nukleus Maven Plugin

[![Build Status][build-status-image]][build-status]

[build-status-image]: https://travis-ci.org/reaktivity/nukleus-maven-plugin.svg?branch=develop
[build-status]: https://travis-ci.org/reaktivity/nukleus-maven-plugin
  
## Rules for using the generated flyweight Builders
 
- builder field mutator methods must be called in the order the fields appear in the IDL, and must be called on all required fields (meaning those with no explicit or implicit default value)

## Default values

- a non-null default value can be specified on int, uint and varint fields, for example: uint8 field1 = 10;
- a null default value can be specified on octets and array fields
- the following types of fields are implicitly defaulted:
  - fields of type list (must appear last in their structure, default to empty)
  - fields of type octets with no specified size (must appear last in their structure, default to empty)
  - fixed width integer fields used to hold the size of a subsequent octets field (automatically set to the correct value when the corresponding octets field is set)
  
## Field types

All of the supported structure member types are illustrated in [test.idl](src/test/resources/test-project/test.idl). Below are otes on some of them.

### Integer types

These comprise fixed width signed types (int8, int16, int32, int64), fixed width unsigned types (uint8, uint16, uint32, uint64) and signed variable width types (varint32, varint64). The variable width types are limited to 4 or 8 bytes in length respectively, and conform to the sint32 and sint64 formats described in https://developers.google.com/protocol-buffers/docs/encoding.

### octets type

The octets type represents an array of bytes. By default the length is undetermined, in which case the field must be the last one in its structure. Alternatively, the length can be fixed, or can be stored in another member of the structure, known as the size field. 

If the size field is a fixed width integer type (int or uint), it is set automatically on mutation of the octets field. If it is a varint (varint32 or varint64), it must be set explicitly before setting the octets field. 

The usages are illustrated in the following structure (taken from [test.idl](src/test/resources/test-project/test.idl)):
```
        struct FlatWithOctets
        {
            uint32 fixed1 = 11;
            octets[10] octets1;
            uint16 lengthOctets2;
            string string1;
            octets[lengthOctets2] octets2;
            varint32 lengthOctets3;
            octets[lengthOctets3] octets3 = null;
            octets extension;
        }
```
- Field octets1 must be exactly 10 bytes long, and the generated mutation methods will fail if not. 
- the size field, lengthOctets2, for field octets2 is read-only. It is set automatically when field octets2 is set.
- field octets3 defaults to null, in which case the size field lengthOctets3 will have the value -1. The size field lengthOctets3 must be set before setting octets3, because it is a varint. When setting the octets3 field an exception will be thrown if the length does not match the value set in lengthOctets3.

### Arrays

#### Arrays of fixed width integers

Arrays of fixed width integers (int or uint) are supported. They can be fixed or variable length. If variable, a size field must be named, which must be one of the fixed width integer types. The usages are illustrated in the following structure:
```
        struct intArrays
        {
            int16 size;
            uint32[3] fixedArray;
            int32[size] variableArray = null;                
        }
```

#### Arrays of variable width integers

Arrays of varints are supported using the syntax illustrated in the following example:
```
        struct varintArrays
        {
            varint32[] varint32Array;                
            varint64[] varint64Array;
        }
```
Specifying a default value is not supported. The actual length in bytes of the array is stored a four byte signed integer (int32), using the byteorder specified in the ArrayFW.Builder or ArrayFW constructor, and is followed by the actual values. Varint array fields are accessed and set via the generated ArrayFW flyweight class. [IntegerVariableArraysFWTest](src/test/java/org/reaktivity/nukleus/maven/plugin/internal/generated/IntegerVariableArraysFWTest.java) illustrates the usage. 
