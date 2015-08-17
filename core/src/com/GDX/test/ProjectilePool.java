package com.GDX.test;

import com.badlogic.gdx.utils.Pool;

public final class ProjectilePool
{	
	static Pool<Projectile> pool = new Pool<Projectile>()
	{
		@Override
		protected Projectile newObject()
		{
			return new Projectile();
		}
	};

	public static Projectile obtain()
	{
		return pool.obtain();
	}
	
	public static void free(Projectile p)
	{
		pool.free(p);
	}
}
