package com.appsdeveloperblog.app.ws.io.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;

import com.appsdeveloperblog.app.ws.io.dao.DAO;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.utils.HibernateUtils;

@SuppressWarnings("deprecation")
public class MySQLDAO implements DAO{
	
	Session session;
	
	public void openConnection(){
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public UserDTO getUserByUserName(String userName){
		
		UserDTO userDto = null;
		
		CriteriaBuilder cb = session.getCriteriaBuilder();
		
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		
		//Query roots always reference entity
		Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
		criteria.select(profileRoot);
		criteria.where(cb.equal(profileRoot.get("email"), userName));
		
		//Fetch single result
		Query<UserEntity> query = session.createQuery(criteria);
		List<UserEntity> resultList = query.getResultList();
		
		if(resultList != null && resultList.size()>0){
			UserEntity userEntity = resultList.get(0);
			userDto = new UserDTO();
			BeanUtils.copyProperties(userEntity,userDto);
		}
		
		return userDto;
	}
	
	public UserDTO getUser(String id){
		CriteriaBuilder cb = session.getCriteriaBuilder();
		
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		
		Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
		criteria.select(profileRoot);
		criteria.where(cb.equal(profileRoot.get("userId"), id));
		
		UserEntity userEntity = session.createQuery(criteria).getSingleResult();
		
		UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDto);
		
		return userDto;
	}
	
	public UserDTO saveUser(UserDTO user){
		UserDTO returnValue = null;
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		session.beginTransaction();
		session.save(userEntity);
		session.getTransaction().commit();
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}
	
	public void updateUser(UserDTO userProfile){
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(userProfile, userEntity);
		
		session.beginTransaction();
		session.update(userEntity);
		session.getTransaction().commit();
	}
	
	public void closeConnection(){
		if(session!=null){
			session.close();
		}
	}
	
	@Override
	public List<UserDTO> getUsers(int start, int limit){
		CriteriaBuilder cb = session.getCriteriaBuilder();
		
		//Create criteria against a particular persistent class
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		
		//Query roots always reference entities
		Root<UserEntity> userRoot = criteria.from(UserEntity.class);
		criteria.select(userRoot);
		
		//Fetch results from start to a number of a limit
		List<UserEntity> searchResults = session.createQuery(criteria).setFirstResult(start).setMaxResults(limit).getResultList();
		
		List<UserDTO> returnValue = new ArrayList<UserDTO>();
		for(UserEntity userEntity : searchResults){
			UserDTO userDto = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}

	@Override
	public void deleteUser(UserDTO userProfile) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userProfile, userEntity);
		
		session.beginTransaction();
		session.delete(userEntity);
		session.getTransaction().commit();
	}
	
}
