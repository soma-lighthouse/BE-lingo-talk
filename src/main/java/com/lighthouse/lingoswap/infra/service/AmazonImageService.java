package com.lighthouse.lingoswap.infra.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.lighthouse.lingoswap.common.util.TimeHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

@Profile("!test")
@Service
public class AmazonImageService implements ImageService {

    private static final long ONE_HOUR_IN_MS = 3_600_000;

    private final AmazonS3 s3;
    private final String bucketName;
    private final String profileKeyPrefix;
    private final String cloudFrontEndpoint;
    private final TimeHolder timeHolder;

    public AmazonImageService(final AmazonS3 s3,
                              @Value("${aws.s3.bucket-name}") final String bucketName,
                              @Value("${aws.s3.profile.prefix}") final String profileKeyPrefix,
                              @Value("${aws.distribution.endpoint}") final String cloudFrontEndpoint,
                              final TimeHolder timeHolder) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.profileKeyPrefix = profileKeyPrefix;
        this.cloudFrontEndpoint = cloudFrontEndpoint;
        this.timeHolder = timeHolder;
    }

    @Override
    public URL generatePresignedUrl(final String key) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, profileKeyPrefix + key, HttpMethod.PUT);
        req.setExpiration(new Date(timeHolder.currentTimeMillis() + ONE_HOUR_IN_MS));
        return s3.generatePresignedUrl(req);
    }

    @Override
    public URL getEndpoint() {
        try {
            return new URL(cloudFrontEndpoint);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

}
