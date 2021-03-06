package crazypants.structures.creator.block.tree;

import java.lang.reflect.Field;
import java.util.List;

import crazypants.structures.api.AttributeDoc;
import crazypants.structures.api.AttributeEditor;

public class ListElementAccessor implements IAttributeAccessor {

  private final int index;
  private final Class<?> type;
  private final String attributeName;
  private final Class<?> declaringClass;
  
  private final String editorType;
  private final String attributeDoc;

  public ListElementAccessor(Class<?> declaringClass, String attributeName, int index, Class<?> type) {
    this.declaringClass = declaringClass;
    this.index = index;
    this.type = type;
    this.attributeName = attributeName;
    
    Field f = null;
    try {
      f = declaringClass.getDeclaredField(attributeName);      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    String ed = null;
    String doc = null;
    if(f != null) {
      AttributeEditor edAn = f.getAnnotation(AttributeEditor.class);
      if(edAn != null) {
        ed = edAn.name();
      }
      AttributeDoc docAn = f.getAnnotation(AttributeDoc.class);
      if(docAn != null) {
        doc = docAn.text();
      }
    }       
    editorType = ed;
    attributeDoc = doc;    
  }

  @Override
  public Object get(Object obj) {
    if(!(obj instanceof List) || index < 0) {
      return null;
    }
    List<?> list = (List<?>) obj;
    if(list.size() >= index) {
      return null;
    }
    return list.get(index);
  }

  @Override
  public void set(Object obj, Object val) {
    if(!(obj instanceof List) || index < 0) {
      return;
    }
    @SuppressWarnings("unchecked")
    List<Object> list = (List<Object>) obj;
    if(index >= list.size()) {
      return;
    }
    list.set(index, val);
  }
    
  public void remove(Object obj) {
    if(!(obj instanceof List) || index < 0) {
      return;
    }
    @SuppressWarnings("unchecked")
    List<Object> list = (List<Object>) obj;
    if(list.size() <= index) {
      return;
    }
    list.remove(index);
  }

  @Override
  public boolean isValid() {    
    return index >= 0;
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public Class<?> getDeclaringClass() {  
    return declaringClass;
  }

  @Override
  public String getAttribuiteName() {
    return attributeName;
  }

  @Override
  public String getEditorType() {
    return editorType;
  }

  @Override
  public String getDocumentation() { 
    return attributeDoc;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
    result = prime * result + index;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null)
      return false;
    if(getClass() != obj.getClass())
      return false;
    ListElementAccessor other = (ListElementAccessor) obj;
    if(attributeName == null) {
      if(other.attributeName != null)
        return false;
    } else if(!attributeName.equals(other.attributeName))
      return false;
    if(index != other.index)
      return false;
    if(type == null) {
      if(other.type != null)
        return false;
    } else if(!type.equals(other.type))
      return false;
    return true;
  }
  
  

}
