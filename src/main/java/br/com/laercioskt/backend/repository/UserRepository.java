package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;

public interface UserRepository extends BaseRepository<User, Long> {

    User findByUserName(String userName);

}

