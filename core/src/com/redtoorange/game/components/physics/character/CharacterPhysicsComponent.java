package com.redtoorange.game.components.physics.character;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.redtoorange.game.components.input.InputComponent;
import com.redtoorange.game.components.physics.PhysicsComponent;
import com.redtoorange.game.components.rendering.SpriteComponent;
import com.redtoorange.game.engine.Updateable;
import com.redtoorange.game.entities.characters.EntityCharacter;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * CharacterPhysicsComponent.java - Generic physics component that can be attached
 * to any entityCharacter.
 *
 * @author - Andrew M.
 * @version - 14/Jan/2017
 */
public abstract class CharacterPhysicsComponent extends PhysicsComponent implements Updateable {
	protected float linearDampening;
	protected float angularDampening;
	protected float density;

	protected EntityCharacter entityCharacter;
	protected InputComponent inputComponent;


	public CharacterPhysicsComponent( PhysicsSystem physicsSystem, EntityCharacter entityCharacter,
									  float speed, float linearDampening, float angularDampening,
									  float density ) {
		super( physicsSystem, entityCharacter );

		this.speed = speed;
		this.linearDampening = linearDampening;
		this.angularDampening = angularDampening;
		this.density = density;
		this.entityCharacter = entityCharacter;

		initPhysics( physicsSystem );
	}

	@Override
	public void update( float deltaTime ) {
		if( inputComponent == null)
			inputComponent = parent.getComponent( InputComponent.class );

		if ( Math.abs( entityCharacter.getRotation( ) - Math.toDegrees( body.getAngle( ) ) ) > 0.01f )
			body.setTransform( body.getPosition( ), ( float ) Math.toRadians( entityCharacter.getRotation( ) ) );

		body.applyLinearImpulse( inputComponent.getDeltaInput().nor( ).scl( speed ), body.getWorldCenter( ), true );
		entityCharacter.getTransform().setPosition( body.getPosition( ) );
	}

	protected void initPhysics( PhysicsSystem physicsSystem ) {
		SpriteComponent sc = parent.getComponent( SpriteComponent.class );

		body = Box2DFactory.createBody( physicsSystem, sc.getBoundingBox( ), BodyDef.BodyType.DynamicBody,
				density, 0f, 0f, false, false );

		body.setUserData( entityCharacter );

		body.setFixedRotation( true );
		body.setLinearDamping( linearDampening );
		body.setAngularDamping( angularDampening );
		body.setSleepingAllowed( false );
	}

	@Override
	public void dispose( ) {
		destroy( );
	}
}