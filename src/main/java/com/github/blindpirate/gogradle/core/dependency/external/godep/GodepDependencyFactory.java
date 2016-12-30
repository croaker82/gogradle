package com.github.blindpirate.gogradle.core.dependency.external.godep;

import com.github.blindpirate.gogradle.core.GolangPackageModule;
import com.github.blindpirate.gogradle.core.InjectionHelper;
import com.github.blindpirate.gogradle.core.dependency.GolangDependencySet;
import com.github.blindpirate.gogradle.core.dependency.parse.MapNotationParser;
import com.github.blindpirate.gogradle.core.dependency.resolve.ExternalDependencyFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.blindpirate.gogradle.util.DataExchange.parseJson;

/**
 * In newest version of godep, it will read dependency versions from GOPATH
 * and lock them into Godeps/Godeps.json.
 *
 * @see <a href="https://github.com/tools/godep">godep</a>
 */
@Singleton
public class GodepDependencyFactory extends ExternalDependencyFactory {
    public static final String GODEPS_DOT_JSON_LOCATION = "Godeps/Godeps.json";

    @Inject
    private MapNotationParser mapNotationParser;

    private List<String> identityFiles = Arrays.asList(GODEPS_DOT_JSON_LOCATION);

    @Override
    protected List<String> identityFiles() {
        return identityFiles;
    }

    @Override
    public Optional<GolangDependencySet> doProduce(GolangPackageModule module) {
        GodepsDotJsonModel model = parse(module);
        return Optional.of(InjectionHelper.parseMany(model.toNotations(), mapNotationParser));
    }

    private GodepsDotJsonModel parse(GolangPackageModule module) {
        File file = module.getRootDir().resolve(GODEPS_DOT_JSON_LOCATION).toFile();
        return parseJson(file, GodepsDotJsonModel.class);
    }

}
