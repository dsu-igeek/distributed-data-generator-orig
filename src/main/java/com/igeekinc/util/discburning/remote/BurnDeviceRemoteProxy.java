/*
 * Copyright 2002-2014 iGeek, Inc.
 * All Rights Reserved
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
 
package com.igeekinc.util.discburning.remote;

import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.igeekinc.util.CheckCorrectDispatchThread;
import com.igeekinc.util.EventHandler;
import com.igeekinc.util.discburning.BurnDevice;
import com.igeekinc.util.discburning.BurnDeviceEvent;
import com.igeekinc.util.discburning.BurnDeviceEventListener;
import com.igeekinc.util.discburning.BurnDeviceStatusChanged;
import com.igeekinc.util.discburning.BurnDeviceStatusChangedListener;
import com.igeekinc.util.discburning.BurnState;
import com.igeekinc.util.discburning.MediaState;
import com.igeekinc.util.discburning.MediaStatus;
import com.igeekinc.util.discburning.MediaType;
import com.igeekinc.util.logging.ErrorLogMessage;

public class BurnDeviceRemoteProxy extends BurnDevice implements BurnDeviceEventListener
{
    RemoteBurnDevice remoteDevice;
    RemoteDiscBurningEventDeliveryImpl remoteEventDelivery;
    
    private final class BurnDeviceStatusChangedEventListenerAdapter implements EventHandler
    {
        BurnDeviceStatusChangedListener listener;
        public BurnDeviceStatusChangedEventListenerAdapter(BurnDeviceStatusChangedListener listener)
        {
            this.listener = listener;
        }
        public void handleEvent(java.util.EventObject eventToHandle) 
        {
            listener.burnDeviceStatusChanged((BurnDeviceStatusChanged)eventToHandle);
        }
        
        public boolean equals(Object checkObject)
        {
            if (!(checkObject instanceof BurnDeviceStatusChangedEventListenerAdapter))
                return false;
            return(((BurnDeviceStatusChangedEventListenerAdapter)checkObject).listener.equals(listener));
        }
    }
    
    public BurnDeviceRemoteProxy(RemoteBurnDevice remoteDevice, CheckCorrectDispatchThread checker)
    throws RemoteException
    {
        super(remoteDevice.getName(), remoteDevice.getID(), checker);
        this.remoteDevice = remoteDevice;
        remoteEventDelivery = new RemoteDiscBurningEventDeliveryImpl(this);
        remoteDevice.setEventDelivery(remoteEventDelivery);
    }

    public BurnState getBurnState()
    {
        try
        {
            return remoteDevice.getBurnState();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return null;
        }
    }

    public void openTray() throws IOException
    {
        try
        {
            remoteDevice.openTray();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
        }
    }

    public void closeTray() throws IOException
    {
        try
        {
            remoteDevice.closeTray();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
        }
    }

    public void ejectMedia() throws IOException
    {
        try
        {
            remoteDevice.ejectMedia();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
        }
    }

    public long getAvailableSpace()
    {
        try
        {
            return remoteDevice.getAvailableSpace();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return 0L;
        }
    }

    public void burnDeviceEvent(BurnDeviceEvent firedEvent)
    {
        deliverySupport.sendEvent(firedEvent);
    }

    public MediaState getMediaState()
    {
        try
        {
            return remoteDevice.getMediaState();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return MediaState.kEmptyState;
        }
    }

    public MediaStatus getMediaStatus()
    {
        try
        {
            return remoteDevice.getMediaStatus();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return MediaStatus.kNoMediaStatus;
        }
    }

    public MediaType getMediaType()
    {
        try
        {
            return remoteDevice.getMediaType();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return MediaType.kMediaTypeNone;
        }
    }

    public double[] getAvailableSpeeds()
    {
        try
        {
            return remoteDevice.getAvailableSpeeds();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return new double[0];
        }
    }

    public boolean acquireExclusiveAccess() throws IOException
    {
        try
        {
            return remoteDevice.acquireExclusiveAccess();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return false;
        }
    }

    public void askForMediaReservation() throws IOException
    {
        try
        {
            remoteDevice.askForMediaReservation();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
        }
    }

    public boolean isMediaReserved()
    {
        try
        {
            return remoteDevice.isMediaReserved();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
            return false;
        }
    }

    public void releaseMediaReservation() throws IOException
    {
        try
        {
            remoteDevice.releaseMediaReservation();
        } catch (RemoteException e)
        {
            Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
        }        
    }
    
   public void releaseExclusiveAccess() throws IOException
    {
       try
       {
           remoteDevice.releaseExclusiveAccess();
       } catch (RemoteException e)
       {
           Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
       }    
    }

   public boolean isAccessExclusive()
   {
       try
       {
           return remoteDevice.isAccessExclusive();
       } catch (RemoteException e)
       {
           Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
           return false;
       }
   }
   public boolean canTrayOpen()
   {
       try
       {
           return remoteDevice.canTrayOpen();
       } catch (RemoteException e)
       {
           Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
           return false;
       }   
   }

   public boolean isTrayOpen()
   {
       try
       {
           return remoteDevice.isTrayOpen();
       } catch (RemoteException e)
       {
           Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
           return false;
       }   
   }

   @Override
   public MediaType[] getSupportedMedia()
   {
       try
       {
           return remoteDevice.getSupportedMedia();
       } catch (RemoteException e)
       {
           Logger.getLogger(getClass()).error(new ErrorLogMessage("Caught exception"), e);
           return new MediaType[0];
       }   
   } 
}
