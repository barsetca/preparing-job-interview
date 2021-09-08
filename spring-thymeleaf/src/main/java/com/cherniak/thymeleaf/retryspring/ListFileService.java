package com.cherniak.thymeleaf.retryspring;

import com.cherniak.thymeleaf.files.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

public class ListFileService {

  private static List<File> files = new ArrayList<>(
      Arrays.asList(new File(1, "One"), new File(2, "Two"), new File(2, "Three")));

  public File getByIndex(int index){
      if (index == 2) {
                  //throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR); // без повтора
                  /*
                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                  1 -> Success(File(id=1, name=One, createDate=null, fileStatus=null))
                  2 -> Success(File(id=2, name=Two, createDate=null, fileStatus=null))
                  3 -> Failure(org.springframework.web.client.HttpServerErrorException: 500 INTERNAL_SERVER_ERROR)
                   */

      // throw new HttpServerErrorException( HttpStatus.SERVICE_UNAVAILABLE); // вкл повтор

      throw new ResourceAccessException(String.format("0 record(s) in file or invalid format of id: %d", index)); // вкл повтор
    }
    return files.get(index);
  }

}
