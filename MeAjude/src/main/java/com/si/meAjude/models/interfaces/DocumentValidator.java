package com.si.meAjude.models.interfaces;

import com.si.meAjude.models.enums.DocumentType;
import com.si.meAjude.models.enums.EntityType;

public interface DocumentValidator {
    void validate(String document, EntityType entityType);

    DocumentType getDocumentType();
}
