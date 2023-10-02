package com.si.meAjude.service;

import com.si.meAjude.models.Usuario;
import com.si.meAjude.service.dtos.usuario.UsuarioDTO;
import com.si.meAjude.service.dtos.usuario.UsuarioUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService{


    public UsuarioDTO save(Usuario object);


    public Page<UsuarioDTO> getAll(Pageable pageable);

    public UsuarioDTO getById(Long id);

    public UsuarioDTO update(UsuarioUpdateDTO object);

    public UsuarioDTO delete(Long id);

    public UsuarioDTO logicDelete(Long id);

    public Page<UsuarioDTO> getAllByDeletedFalse(Pageable pageable);

}
