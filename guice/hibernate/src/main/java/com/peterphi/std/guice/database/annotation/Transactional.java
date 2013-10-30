package com.peterphi.std.guice.database.annotation;

/*
 * Based on warp-persist Transactional which is originally
 * 
 * Copyright (C) 2008 Wideplay Interactive.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional
{
	public boolean readOnly() default false;

	/**
	 * A list of exceptions to rollback on, if thrown by the transactional method. These exceptions are propagated correctly after a rollback.
	 * 
	 * @return Returns the configured rollback exceptions.
	 */
	Class<? extends Exception>[] rollbackOn() default RuntimeException.class;

	/**
	 * A list of exceptions to *not* rollback on. A caveat to the rollbackOn clause.
	 * 
	 * The disjunction of rollbackOn and exceptOn represents the list of exceptions that will trigger a rollback. The complement of rollbackOn and the universal set plus any exceptions in the exceptOn
	 * set represents the list of exceptions that will trigger a commit.
	 * <p/>
	 * Note that exceptOn exceptions take precedence over rollbackOn, but with subtype granularity.
	 * 
	 * @return Returns the configured rollback exceptions.
	 */
	Class<? extends Exception>[] exceptOn() default {};
}