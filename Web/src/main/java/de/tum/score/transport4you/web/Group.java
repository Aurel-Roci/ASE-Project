package de.tum.score.transport4you.web;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Group {
  @Id public String group;
}
