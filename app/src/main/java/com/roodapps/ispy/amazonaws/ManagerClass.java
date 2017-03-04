package com.roodapps.ispy.amazonaws;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.List;

/**
 * Created by Ethan on 26/02/2017.
 */

public class ManagerClass
{
    DynamoDBMapper mapper;
    AmazonDynamoDBClient ddbClient;

    AmazonS3 s3;
    TransferUtility transferUtility;

    public CognitoCachingCredentialsProvider getCredentials(Context context)
    {
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "ap-southeast-2:25ffcacf-9902-4151-bd93-bb508fd321f0", // Identity Pool ID
                Regions.AP_SOUTHEAST_2 // Region
        );

        return credentialsProvider;
    }

    // SETTERS
    public void setDdbClient(CognitoCachingCredentialsProvider credentialsProvider)
    {
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
    }

    public void setMapper(AmazonDynamoDBClient ddbClient)
    {
        mapper = new DynamoDBMapper(ddbClient);
    }

    public void setS3Bucket(CognitoCachingCredentialsProvider credentialsProvider)
    {
        s3 = new AmazonS3Client(credentialsProvider);
//        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
    }

    public void setTransferUtility(Context context, AmazonS3 s3)
    {
        transferUtility = new TransferUtility(s3, context);
    }


    // GETTERS
    public AmazonDynamoDBClient getDdbClient()
    {
        return this.ddbClient;
    }
    public DynamoDBMapper getMapper()
    {
        return this.mapper;
    }

    public AmazonS3 getS3() { return this.s3; }
    public TransferUtility getTransferUtility() { return this.transferUtility; }
}
