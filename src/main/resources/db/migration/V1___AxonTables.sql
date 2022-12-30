CREATE TABLE `AssociationValueEntry` (
    `id` bigint NOT NULL,
    `associationKey` varchar(255) NOT NULL,
    `associationValue` varchar(255) DEFAULT NULL,
    `sagaId` varchar(255) NOT NULL,
    `sagaType` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX2uqqpmht3w2i368ld2ham2out` (`sagaType`,`associationKey`,`associationValue`),
    KEY `IDXpo4uvnt1l3922m6y62fk73p3f` (`sagaId`,`sagaType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `DeadLetterEntry` (
     `deadLetterId` varchar(255) NOT NULL,
     `causeMessage` varchar(255) DEFAULT NULL,
     `causeType` varchar(255) DEFAULT NULL,
     `diagnostics` longblob,
     `enqueuedAt` datetime(6) NOT NULL,
     `lastTouched` datetime(6) DEFAULT NULL,
     `aggregateIdentifier` varchar(255) DEFAULT NULL,
     `eventIdentifier` varchar(255) NOT NULL,
     `messageType` varchar(255) NOT NULL,
     `metaData` longblob,
     `payload` longblob NOT NULL,
     `payloadRevision` varchar(255) DEFAULT NULL,
     `payloadType` varchar(255) NOT NULL,
     `sequenceNumber` bigint DEFAULT NULL,
     `timeStamp` varchar(255) NOT NULL,
     `token` longblob,
     `tokenType` varchar(255) DEFAULT NULL,
     `type` varchar(255) DEFAULT NULL,
     `processingGroup` varchar(255) NOT NULL,
     `processingStarted` datetime(6) DEFAULT NULL,
     `sequenceIdentifier` varchar(255) NOT NULL,
     `sequenceIndex` bigint NOT NULL,
     PRIMARY KEY (`deadLetterId`),
     UNIQUE KEY `UK29kmqmg0mck0isn98myilsvls` (`processingGroup`,`sequenceIdentifier`,`sequenceIndex`),
     KEY `IDX1g6rwlb6q8u1s8p55hcmq2wdh` (`processingGroup`),
     KEY `IDXiq9woyp54f179nfbl60dcelr` (`processingGroup`,`sequenceIdentifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `DomainEventEntry` (
     `globalIndex` bigint NOT NULL,
     `eventIdentifier` varchar(255) NOT NULL,
     `metaData` longblob,
     `payload` longblob NOT NULL,
     `payloadRevision` varchar(255) DEFAULT NULL,
     `payloadType` varchar(255) NOT NULL,
     `timeStamp` varchar(255) NOT NULL,
     `aggregateIdentifier` varchar(255) NOT NULL,
     `sequenceNumber` bigint NOT NULL,
     `type` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`globalIndex`),
     UNIQUE KEY `UKdg43ia27ypo1jovw2x64vbwv8` (`aggregateIdentifier`,`sequenceNumber`),
     UNIQUE KEY `UK_k5lt6d2792amnloo7q91njp0v` (`eventIdentifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `SagaEntry` (
     `sagaId` varchar(255) NOT NULL,
     `revision` varchar(255) DEFAULT NULL,
     `sagaType` varchar(255) DEFAULT NULL,
     `serializedSaga` longblob,
     PRIMARY KEY (`sagaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `SnapshotEventEntry` (
      `aggregateIdentifier` varchar(255) NOT NULL,
      `sequenceNumber` bigint NOT NULL,
      `type` varchar(255) NOT NULL,
      `eventIdentifier` varchar(255) NOT NULL,
      `metaData` longblob,
      `payload` longblob NOT NULL,
      `payloadRevision` varchar(255) DEFAULT NULL,
      `payloadType` varchar(255) NOT NULL,
      `timeStamp` varchar(255) NOT NULL,
      PRIMARY KEY (`aggregateIdentifier`,`sequenceNumber`,`type`),
      UNIQUE KEY `UK_sg7xx45yh4ajlsjd8t0uygnjn` (`eventIdentifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `TokenEntry` (
      `processorName` varchar(255) NOT NULL,
      `segment` int NOT NULL,
      `owner` varchar(255) DEFAULT NULL,
      `timestamp` varchar(255) NOT NULL,
      `token` longblob,
      `tokenType` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`processorName`,`segment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;