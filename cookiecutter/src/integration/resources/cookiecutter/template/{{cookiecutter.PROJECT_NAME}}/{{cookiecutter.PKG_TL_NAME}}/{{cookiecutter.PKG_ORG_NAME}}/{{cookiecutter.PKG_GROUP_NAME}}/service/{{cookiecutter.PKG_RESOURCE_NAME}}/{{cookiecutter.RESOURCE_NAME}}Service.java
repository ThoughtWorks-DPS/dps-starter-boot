package {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.service.{{cookiecutter.PKG_RESOURCE_NAME}};

import {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.model.{{cookiecutter.PKG_RESOURCE_NAME}}.{{cookiecutter.RESOURCE_NAME}}Model;

import java.util.List;
import java.util.Optional;

public interface {{cookiecutter.RESOURCE_NAME}}Service {

  {{cookiecutter.RESOURCE_NAME}}Model add({{cookiecutter.RESOURCE_NAME}}Model record);

  List<{{cookiecutter.RESOURCE_NAME}}Model> findByLastName(String lastName);

  Optional<{{cookiecutter.RESOURCE_NAME}}Model> findByUserName(String userName);

  Optional<{{cookiecutter.RESOURCE_NAME}}Model> findById(String id);

  List<{{cookiecutter.RESOURCE_NAME}}Model> findAll();

  Optional<{{cookiecutter.RESOURCE_NAME}}Model> updateById(String id, {{cookiecutter.RESOURCE_NAME}}Model record);

  Optional<{{cookiecutter.RESOURCE_NAME}}Model> deleteById(String id);
}
